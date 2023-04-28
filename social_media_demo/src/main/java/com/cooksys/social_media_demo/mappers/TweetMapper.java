package com.cooksys.social_media_demo.mappers;


import com.cooksys.social_media_demo.dtos.TweetRequestDto;
import com.cooksys.social_media_demo.dtos.TweetResponseDto;
import com.cooksys.social_media_demo.entities.Tweet;
import org.mapstruct.Mapper;

import java.util.List;

import com.cooksys.social_media_demo.dtos.CredentialsDto;


@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface TweetMapper {

    Tweet requestDtoToEntity(TweetRequestDto tweetRequestDto);

    List<TweetResponseDto> entitiesToResponseDtos(List<Tweet> activeMentions);

    TweetResponseDto entityToResponseDto(Tweet tweet);
    
    List<TweetResponseDto> entitiesToDto(List<Tweet> tweets);
    
    Tweet credentialsDtoToEntity (CredentialsDto credentialsDto);

    List<TweetResponseDto> entitiesToDtos(List<Tweet> tweetList);
}