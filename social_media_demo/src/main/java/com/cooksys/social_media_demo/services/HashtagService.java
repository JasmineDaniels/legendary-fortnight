package com.cooksys.social_media_demo.services;

import java.util.List;

import com.cooksys.social_media_demo.dtos.HashtagDto;
import com.cooksys.social_media_demo.dtos.TweetResponseDto;

public interface HashtagService {

	List<HashtagDto> getTags();
  
    List<TweetResponseDto> getAllTweetsByTag(String label);

}
