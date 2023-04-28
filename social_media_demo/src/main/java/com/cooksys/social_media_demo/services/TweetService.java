package com.cooksys.social_media_demo.services;

import com.cooksys.social_media_demo.dtos.TweetRequestDto;
import com.cooksys.social_media_demo.dtos.ContextDto;
import com.cooksys.social_media_demo.dtos.CredentialsDto;
import com.cooksys.social_media_demo.dtos.HashtagDto;
import com.cooksys.social_media_demo.dtos.TweetResponseDto;
import com.cooksys.social_media_demo.dtos.UserResponseDto;

import java.util.List;

public interface TweetService {
    List<TweetResponseDto> getAllTweets();

    TweetResponseDto createTweet(TweetRequestDto tweetRequestDto);

    TweetResponseDto getTweetById(Long id);

    TweetResponseDto deleteTweetById(Long id, CredentialsDto credentialsDto);

    void likeTweetById(Long id, CredentialsDto credentialsDto);

	ContextDto getTweetContextFromId(Long id);

	TweetResponseDto createRepost(Long id, CredentialsDto credentialsDto);

	List<HashtagDto> getTagsById(Long id);

	List<UserResponseDto> getLikes(Long id);

    TweetResponseDto postReplyTweet(Long id, TweetRequestDto tweetRequestDto);

    List<TweetResponseDto> getReposts(Long id);

    List<UserResponseDto> getUsersMentioned(Long id);

    List<TweetResponseDto> getRepliesById(Long id);
}
