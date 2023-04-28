package com.cooksys.social_media_demo.services;

import java.util.List;

import com.cooksys.social_media_demo.dtos.CredentialsDto;
import com.cooksys.social_media_demo.dtos.TweetResponseDto;
import com.cooksys.social_media_demo.dtos.UserRequestDto;
import com.cooksys.social_media_demo.dtos.UserResponseDto;

public interface UserService {

    UserResponseDto createUser(UserRequestDto userRequestDto);

    List<UserResponseDto> getFollowersByUsername(String username);

    List<UserResponseDto> getFollowingByUsername(String username);

    List<TweetResponseDto> getMentionsByUsername(String username);

	UserResponseDto getUserByUsername(String username);

	UserResponseDto updateUsername(String username, UserRequestDto userRequestDto);

	UserResponseDto deleteUser(String username, CredentialsDto credentialsDto);

	List<TweetResponseDto> getTweetsByUsername(String username);
	
    List<UserResponseDto> getAllUsers();

    UserResponseDto followAUser(String username, CredentialsDto credentialsDto);

    UserResponseDto unfollowAUser(String username, CredentialsDto credentialsDto);

    List<TweetResponseDto> getTwitterFeed(String username);
}
