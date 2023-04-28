package com.cooksys.social_media_demo.services.impl;


import com.cooksys.social_media_demo.dtos.TweetResponseDto;
import com.cooksys.social_media_demo.dtos.UserResponseDto;
import com.cooksys.social_media_demo.entities.Tweet;
import com.cooksys.social_media_demo.exceptions.BadRequestException;
import com.cooksys.social_media_demo.exceptions.NotFoundException;
import com.cooksys.social_media_demo.mappers.CredentialsMapper;
import com.cooksys.social_media_demo.mappers.ProfileMapper;
import com.cooksys.social_media_demo.mappers.TweetMapper;
import com.cooksys.social_media_demo.services.UserService;
import com.cooksys.social_media_demo.utilities.Utilities;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.social_media_demo.dtos.CredentialsDto;

import com.cooksys.social_media_demo.dtos.UserRequestDto;


import com.cooksys.social_media_demo.entities.User;



import com.cooksys.social_media_demo.mappers.UserMapper;
import com.cooksys.social_media_demo.repositories.TweetRepository;
import com.cooksys.social_media_demo.repositories.UserRepository;


import lombok.RequiredArgsConstructor;

import java.util.Comparator;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	
	private final UserRepository userRepository;
	private final UserMapper userMapper;

	private final TweetRepository tweetRepository;
	private final TweetMapper tweetMapper;

	private final ProfileMapper profileMapper;

	private final CredentialsMapper credentialsMapper;

	private final Utilities utilities;


	@Override
	public UserResponseDto createUser(UserRequestDto userRequestDto) {
		//Checks Profile first for missing fields
		utilities.checkProfile(userRequestDto.getProfile());

		//Check Credentials for missing fields
		utilities.checkCredentials(userRequestDto.getCredentials());

		//Checks for existing users
		Optional<User> existingUser = userRepository.findByCredentials_Username(userRequestDto.getCredentials().getUsername());

		//If user doesn't exist then check if email was used
		if (existingUser.isEmpty()) {
			Optional<User> emailUser = userRepository.findByProfile_Email(userRequestDto.getProfile().getEmail());
			//If email is used then cannot create
			if (emailUser.isPresent()) {
				throw new BadRequestException("Email already being used");
			} else {
				User newUser = userRepository.saveAndFlush(userMapper.requestDtoToEntity(userRequestDto));
				return userMapper.entityToResponseDto(newUser);
			}
		} else {
			//If deleted then re-activate
			if (existingUser.get().isDeleted()) {
				//If credentials and email match can reactivate
				if (profileMapper.dtoToEntity(userRequestDto.getProfile()).equals(existingUser.get().getProfile())
						&& credentialsMapper.dtoToEntity(userRequestDto.getCredentials()).equals(existingUser.get().getCredentials())) {
					existingUser.get().setDeleted(false);
					return userMapper.entityToResponseDto(userRepository.saveAndFlush(existingUser.get()));
				} else {
					throw new BadRequestException("Email already being used");
				}
			} else {
				//If not deleted
				throw new BadRequestException("Username already taken");
			}
		}
	}


	@Override
	public UserResponseDto getUserByUsername(String username) {
		// find user in repository by credentials_username
		Optional<User> userToFind = userRepository.findByCredentials_Username(username);
		// verify user exists
		if (userToFind.isEmpty()) {
			throw new NotFoundException("Error: user with username @" + username + " doesn't exist.");
		}

		// create user entity and verify isn't deleted
		User existingUser = userRepository.findByCredentials_Username(username).get();
		if (existingUser.isDeleted()) {
			throw new NotFoundException("Error: User with username @" + username + " does not exist.");
		} else {
			return userMapper.entityToResponseDto(existingUser);
		}
	}
	
	@Override
	public List<TweetResponseDto> getTweetsByUsername(String username) {
		
		// check user is valid
		utilities.checkUserByUsername(username);
		
		// create user entity and get list of tweets
		User existingUser = userRepository.findByCredentials_Username(username).get();
		List<Tweet> tweets = existingUser.getTweets();
		
		// check tweets aren't deleted and add to new list
		List<Tweet> nonDeletedTweets = new ArrayList<>();
		for (Tweet tweet : tweets) {
			if (!tweet.isDeleted()) {
				nonDeletedTweets.add(tweet);
			}
		}
		
		return tweetMapper.entitiesToDto(nonDeletedTweets);
	}

	@Override
	public UserResponseDto updateUsername(String username, UserRequestDto userRequestDto) {
		if (userRequestDto.getProfile() == null) {
			throw new BadRequestException("Profile cannot be null");
		}
		utilities.checkCredentials(userRequestDto.getCredentials());
		utilities.checkIsActiveUser(userRequestDto.getCredentials());

		User userToFind = userMapper.requestDtoToEntity(userRequestDto);
		User existingUser = userRepository.findByCredentials_Username(userToFind.getCredentials().getUsername()).get();

		// Create User entity from Repo, set Credentials.Username, save it in Repo
		existingUser.getCredentials().setUsername(username);
		userRepository.saveAndFlush(existingUser);

		return userMapper.entityToResponseDto(existingUser);

	}

	@Override
	public UserResponseDto deleteUser(String username, CredentialsDto credentialsDto) {

		utilities.checkCredentials(credentialsDto);
		utilities.checkIsActiveUser(credentialsDto);

		if (!username.equals(credentialsDto.getUsername())) {
			throw new BadRequestException("Username doesn't match");
		}

		User userToDelete = userRepository.findByCredentials_Username(username).get();
		userToDelete.setDeleted(true);
		for (Tweet tweet : userToDelete.getTweets()) {
			tweet.setDeleted(true);
			tweetRepository.saveAndFlush(tweet);
		}
		userRepository.saveAndFlush(userToDelete);

		return userMapper.entityToResponseDto(userToDelete);
	}


	@Override
	public List<UserResponseDto> getAllUsers() {
		List<User> allUsers = userRepository.findAll();
		List<User> activeUsers = new ArrayList<>();

		for (User user : allUsers) {
			if (!user.isDeleted()) {
				activeUsers.add(user);
			}
		}

		return userMapper.entitiesToResponseDtos(activeUsers);
	}

	@Override
	public UserResponseDto followAUser(String username, CredentialsDto credentialsDto) {
		utilities.checkUsernameNullValues(username);
		//check if follower is a valid, authenticated user
		utilities.checkCredentials(credentialsDto);
		utilities.checkIsActiveUser(credentialsDto);
		User follower = userRepository.findByCredentials_Username(credentialsDto.getUsername()).get();

		// check if the user to follow is a valid user
		Optional<User> userToFollowIsValid = userRepository.findByCredentials_Username(username);

		if (userToFollowIsValid.isEmpty() || userToFollowIsValid.get().isDeleted()) {
			throw new NotFoundException("Sorry, user:" + username + ", is not a valid user.");
		} else {
			// Get all followers from the user to follow list of followers
			List<User> userToFollowListOfFriends = userToFollowIsValid.get().getFollowers();
			if(userToFollowListOfFriends.contains(follower)){
				throw new BadRequestException("You already follow " + username + ".");
			} else {
				// Add the follower to the user to follow list of followers
				userToFollowListOfFriends.add(follower);

				// Add the user to follow to the followers list of followers they're following
				List<User> followers_followingList = follower.getFollowing();
				followers_followingList.add(userToFollowIsValid.get());

				// Update both users in the database
				userRepository.saveAndFlush(follower);
				User updatedUserToFollow = userRepository.saveAndFlush(userToFollowIsValid.get());
				return userMapper.entityToResponseDto(updatedUserToFollow);
			}
		}
	}


	@Override
	public UserResponseDto unfollowAUser(String username, CredentialsDto credentialsDto) {
		utilities.checkUsernameNullValues(username);
		//check if follower is a valid, authenticated user
		utilities.checkCredentials(credentialsDto);
		utilities.checkIsActiveUser(credentialsDto);
		User follower = userRepository.findByCredentials_Username(credentialsDto.getUsername()).get();

		// check if the user to unfollow is a valid user
		Optional<User> userToUnfollowIsValid = userRepository.findByCredentials_Username(username);

		if (userToUnfollowIsValid.isEmpty() || userToUnfollowIsValid.get().isDeleted()) {
			throw new NotFoundException("Sorry, user:" + username + ", is not a valid user.");
		} else {
			// Get all followers from the user to unfollow list of followers
			List<User> userToUnfollowListOfFriends = userToUnfollowIsValid.get().getFollowers();
			if(!userToUnfollowListOfFriends.contains(follower)){
				throw new BadRequestException("You do not follow " + username + ".");
			} else {
				// remove follower from the user to unfollow list of followers
				userToUnfollowListOfFriends.remove(follower);

				// remove user to unfollow from the followers list of followers they're following
				List<User> followers_following_list = follower.getFollowing();
				followers_following_list.remove(userToUnfollowIsValid.get());

				// Update both users in the database
				userRepository.saveAndFlush(follower);
				User updatedUser = userRepository.saveAndFlush(userToUnfollowIsValid.get());
				return userMapper.entityToResponseDto(updatedUser);
			}
		}
	}

	@Override
	public List<UserResponseDto> getFollowersByUsername(String username) {
		if (username == null || username.equals("")) {
			throw new BadRequestException("Username cannot be missing or empty");
		}
		Optional<User> existingUser = userRepository.findByCredentials_Username(username);

		//If user doesn't exist
		if (existingUser.isEmpty()) {
			throw new NotFoundException("Error: User with username @" + username + " does not exist.");
		} else {
			//If user was deleted
			if (existingUser.get().isDeleted()) {
				throw new NotFoundException("Error: User with username @" + username + " does not exist.");
			} else {
				List<User> activeFollowers = new ArrayList<>();

				for (User user : existingUser.get().getFollowers()) {
					if (!user.isDeleted()) {
						activeFollowers.add(user);
					}
				}

				return userMapper.entitiesToResponseDtos(activeFollowers);
			}
		}
	}

	@Override
	public List<UserResponseDto> getFollowingByUsername(String username) {
		utilities.checkUsernameNullValues(username);
		utilities.checkUserByUsername(username);
		User existingUser = userRepository.findByCredentials_Username(username).get();

		List<User> activeFollowing = new ArrayList<>();

		for (User user : existingUser.getFollowing()) {
			if (!user.isDeleted()) {
				activeFollowing.add(user);
			}
		}

		return userMapper.entitiesToResponseDtos(activeFollowing);
	}

	@Override
	public List<TweetResponseDto> getMentionsByUsername(String username) {
		utilities.checkUsernameNullValues(username);
		utilities.checkUserByUsername(username);
		Optional<User> existingUser = userRepository.findByCredentials_Username(username);

		List<Tweet> activeMentions = new ArrayList<>();

		for (Tweet tweet : existingUser.get().getMentionedTweets()) {
			if (!tweet.isDeleted()) {
				if (!tweet.getAuthor().isDeleted()) {
					activeMentions.add(tweet);
				}
			}
		}

		Comparator<Tweet> tweetPostedComparator = Comparator.comparing(Tweet::getPosted);

		activeMentions.sort(tweetPostedComparator.reversed());

		return tweetMapper.entitiesToResponseDtos(activeMentions);
	}

	@Override
	public List<TweetResponseDto> getTwitterFeed(String username) {
		utilities.checkUsernameNullValues(username);
		//check if username provided is a valid user
		utilities.checkUserByUsername(username);

		User user = userRepository.findByCredentials_Username(username).get();
		//find All Tweets by this user
		List<Tweet> usersFeed = tweetRepository.findAllByAuthorAndDeletedFalseOrderByPostedDesc(user);
		// Get all users this user is following
		List<User> listOfFriends = user.getFollowing();
		// hold All tweets by the users this user is following
		List<List<Tweet>> usersFriendsTweets = new ArrayList<>();
		for (User u : listOfFriends){
			usersFriendsTweets.add(tweetRepository.findAllByAuthorAndDeletedFalseOrderByPostedDesc(u));
			for(List<Tweet> friendsTweets : usersFriendsTweets){
				usersFeed.addAll(friendsTweets);
			}
		}

		return tweetMapper.entitiesToDtos(usersFeed);
	}

}
