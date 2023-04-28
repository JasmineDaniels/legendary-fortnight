package com.cooksys.social_media_demo.services.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.cooksys.social_media_demo.dtos.CredentialsDto;
import com.cooksys.social_media_demo.dtos.TweetRequestDto;
import com.cooksys.social_media_demo.dtos.TweetResponseDto;
import com.cooksys.social_media_demo.entities.Hashtag;
import com.cooksys.social_media_demo.entities.Tweet;
import com.cooksys.social_media_demo.entities.User;
import com.cooksys.social_media_demo.exceptions.BadRequestException;
import com.cooksys.social_media_demo.exceptions.NotAuthorizedException;
import com.cooksys.social_media_demo.mappers.CredentialsMapper;
import com.cooksys.social_media_demo.mappers.HashtagMapper;
import com.cooksys.social_media_demo.mappers.TweetMapper;
import com.cooksys.social_media_demo.repositories.HashtagRepository;
import com.cooksys.social_media_demo.repositories.TweetRepository;
import com.cooksys.social_media_demo.repositories.UserRepository;
import com.cooksys.social_media_demo.services.TweetService;
import com.cooksys.social_media_demo.utilities.Utilities;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.cooksys.social_media_demo.dtos.ContextDto;

import com.cooksys.social_media_demo.dtos.HashtagDto;

import com.cooksys.social_media_demo.dtos.UserResponseDto;

import com.cooksys.social_media_demo.mappers.UserMapper;


@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final TweetRepository tweetRepository;
    private final TweetMapper tweetMapper;

    private final HashtagRepository hashtagRepository;
    private final HashtagMapper hashtagMapper;

    private final CredentialsMapper credentialsMapper;

    private final Utilities utilities;


    @Override
    public List<TweetResponseDto> getAllTweets() {
        List<Tweet> allTweets = tweetRepository.findAll();
        List<Tweet> activeTweets = new ArrayList<>();

        for (Tweet tweet : allTweets) {
            if (!tweet.isDeleted()) {
                activeTweets.add(tweet);
            }
        }

        Comparator<Tweet> tweetPostedComparator = Comparator.comparing(Tweet::getPosted);

        activeTweets.sort(tweetPostedComparator.reversed());

        return tweetMapper.entitiesToResponseDtos(activeTweets);
    }

    @Override
    public TweetResponseDto createTweet(TweetRequestDto tweetRequestDto) {
        //Check Credentials for missing fields
        utilities.checkCredentials(tweetRequestDto.getCredentials());

        //Check for active user
        utilities.checkIsActiveUser(tweetRequestDto.getCredentials());

        //Determining mentions and hashtags if content exists
        if (tweetRequestDto.getContent() != null) {
            User author = userRepository.findByCredentials_Username(tweetRequestDto.getCredentials().getUsername()).get();

            //Creating tweet and setting author
            Tweet tweetToCreate = tweetRepository.saveAndFlush(tweetMapper.requestDtoToEntity(tweetRequestDto));
            tweetToCreate.setAuthor(author);

            //Get all words from content
            String[] words = tweetRequestDto.getContent().split("\\r\\n|\\r|\\n| ");

            List<User> mentionedUsers = new ArrayList<>();
            List<Hashtag> hashtags = new ArrayList<>();

            for (String word : words) {
                if (word.charAt(0) == '@') {
                    String username = word.substring(1);
                    if (userRepository.findByCredentials_Username(username).isPresent()) {
                        mentionedUsers.add(userRepository.findByCredentials_Username(username).get());
                    }
                } else if (word.charAt(0) == '#') {
                    String label = word.substring(1);
                    if (hashtagRepository.findByLabel(label).isPresent()) {
                        hashtags.add(hashtagRepository.findByLabel(label).get());
                    } else {
                        hashtags.add(hashtagRepository.saveAndFlush(hashtagMapper.labelToEntity(label)));
                    }
                }
            }

            tweetToCreate.setMentionedUsers(mentionedUsers);
            tweetToCreate.setHashtags(hashtags);

            //Saving tweet in repository
            return tweetMapper.entityToResponseDto(tweetRepository.saveAndFlush(tweetToCreate));
        } else {
            throw new BadRequestException("Content cannot be null");
        }
    }

    @Override
    public TweetResponseDto getTweetById(Long id) {
        utilities.checkIsActiveTweet(id);

        return tweetMapper.entityToResponseDto(tweetRepository.findById(id).get());
    }

    @Override
    public TweetResponseDto deleteTweetById(Long id, CredentialsDto credentialsDto) {
        //Check Credentials for missing fields
        utilities.checkCredentials(credentialsDto);

        //Checks for active user
        utilities.checkIsActiveUser(credentialsDto);

        utilities.checkIsActiveTweet(id);

        Tweet tweetToBeDeleted = tweetRepository.findById(id).get();

        if (tweetToBeDeleted.getAuthor().getCredentials().equals(credentialsMapper.dtoToEntity(credentialsDto))) {
            tweetToBeDeleted.setDeleted(true);
            return tweetMapper.entityToResponseDto(tweetRepository.saveAndFlush(tweetToBeDeleted));
        } else {
            throw new NotAuthorizedException("Incorrect Author Credentials");
        }
    }

	@Override
	public ContextDto getTweetContextFromId(Long id) {
        utilities.checkIsActiveTweet(id);

		ContextDto context = new ContextDto();

		Tweet tweetToFind = tweetRepository.findById(id).get();

		Tweet beforeTweet = tweetToFind.getInReplyTo();
		List<Tweet> repliesBefore = new ArrayList<>();
		while (beforeTweet != null) {
			repliesBefore.add(beforeTweet);
			beforeTweet = beforeTweet.getInReplyTo();
		}

		Comparator<Tweet> beforeTweetsSorted = Comparator.comparing(Tweet::getPosted);
		repliesBefore.sort(beforeTweetsSorted);

		List<Tweet> afterTweets = tweetToFind.getReplies();
        List<Tweet> after = new ArrayList<>(afterTweets);
		for (Tweet tweet : afterTweets) {
			if (tweet.getReplies() != null) {
				after.addAll(tweet.getReplies());
			}
		}

        repliesBefore.removeIf(Tweet::isDeleted);

        after.removeIf(Tweet::isDeleted);

		after.sort(beforeTweetsSorted);

		context.setBefore(tweetMapper.entitiesToDto(repliesBefore));
		context.setTarget(tweetMapper.entityToResponseDto(tweetToFind));
		context.setAfter(tweetMapper.entitiesToDto(after));

		return context;

	}

	@Override
	public TweetResponseDto createRepost(Long id, CredentialsDto credentialsDto) {
		utilities.checkCredentials(credentialsDto);
		utilities.checkIsActiveUser(credentialsDto);
		utilities.checkIsActiveTweet(id);

		Tweet tweetToRepost = tweetRepository.findById(id).get();

        User userReposting = userRepository.findByCredentials_Username(credentialsDto.getUsername()).get();

		Tweet repostRequest = tweetRepository.saveAndFlush(tweetMapper.credentialsDtoToEntity(credentialsDto));
		repostRequest.setContent(null);
		repostRequest.setAuthor(userReposting);

        repostRequest.setRepostOf(tweetToRepost);

		return tweetMapper.entityToResponseDto(tweetRepository.saveAndFlush(tweetToRepost));
	}

	@Override
	public List<HashtagDto> getTagsById(Long id) {
		utilities.checkIsActiveTweet(id);

		Tweet existingTweet = tweetRepository.findById(id).get();
		return hashtagMapper.entitiesToDto(existingTweet.getHashtags());
	}

	@Override
	public List<UserResponseDto> getLikes(Long id) {
		utilities.checkIsActiveTweet(id);

		Tweet existingTweet = tweetRepository.findById(id).get();
		List<User> likedBy = new ArrayList<>();
		for (User user : existingTweet.getLikedByUsers()) {
			if (!user.isDeleted()) {
				likedBy.add(user);
			}
		}
		return userMapper.entitiesToResponseDtos(likedBy);
	}

    @Override
    public TweetResponseDto postReplyTweet(Long id, TweetRequestDto tweetRequestDto) {
        //Check if the user is a valid, authenticated user
        utilities.checkCredentials(tweetRequestDto.getCredentials());
        utilities.checkIsActiveUser(tweetRequestDto.getCredentials());
        utilities.checkIsActiveTweet(id);

        Tweet tweetToReplyTo = tweetRepository.findById(id).get();

        TweetResponseDto replyTweetDto = createTweet(tweetRequestDto);

        Tweet replyTweet = tweetRepository.findById(replyTweetDto.getId()).get();

        replyTweet.setInReplyTo(tweetToReplyTo);

        return tweetMapper.entityToResponseDto(tweetRepository.saveAndFlush(replyTweet));
    }

    @Override
    public void likeTweetById(Long id, CredentialsDto credentialsDto) {
        //Check Credentials for missing fields
        utilities.checkCredentials(credentialsDto);

        //Checks for active user
        utilities.checkIsActiveUser(credentialsDto);

        utilities.checkIsActiveTweet(id);

        User existingUser = userRepository.findByCredentials_Username(credentialsDto.getUsername()).get();

        Tweet tweetToLike = tweetRepository.findById(id).get();

        if (!tweetToLike.getLikedByUsers().contains(existingUser)) {
            List<Tweet> likedTweets = existingUser.getLikedTweets();
            likedTweets.add(tweetToLike);
            existingUser.setLikedTweets(likedTweets);
            userRepository.saveAndFlush(existingUser);
        }
    }

    public List<TweetResponseDto> getReposts(Long id) {
        utilities.checkIsActiveTweet(id);

        List<Tweet> reposts = new ArrayList<>();

        Tweet mainTweet = tweetRepository.findById(id).get();

        for (Tweet tweet : mainTweet.getReposts()) {
            if (!tweet.isDeleted()) {
                reposts.add(tweet);
            }
        }

        return tweetMapper.entitiesToDtos(reposts);
    }

    @Override
    public List<TweetResponseDto> getRepliesById(Long id) {
        utilities.checkIsActiveTweet(id);

        List<Tweet> replies = new ArrayList<>();

        Tweet mainTweet = tweetRepository.findById(id).get();

        for (Tweet tweet : mainTweet.getReplies()) {
            if (!tweet.isDeleted()) {
                replies.add(tweet);
            }
        }

        return tweetMapper.entitiesToDtos(replies);
    }

    @Override
    public List<UserResponseDto> getUsersMentioned(Long id) {
        //check if tweet is valid
        utilities.checkIsActiveTweet(id);

        List<User> mentionedUsers = new ArrayList<>();

        Tweet mainTweet = tweetRepository.findById(id).get();

        for (User user : mainTweet.getMentionedUsers()) {
            if (!user.isDeleted()) {
                mentionedUsers.add(user);
            }
        }

        return userMapper.entitiesToResponseDtos(mentionedUsers);
    }
}
