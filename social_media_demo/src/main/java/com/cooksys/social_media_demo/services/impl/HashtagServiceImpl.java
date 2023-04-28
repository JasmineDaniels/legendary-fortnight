package com.cooksys.social_media_demo.services.impl;


import com.cooksys.social_media_demo.dtos.HashtagDto;

import com.cooksys.social_media_demo.dtos.TweetResponseDto;
import com.cooksys.social_media_demo.entities.Hashtag;
import com.cooksys.social_media_demo.entities.Tweet;
import com.cooksys.social_media_demo.exceptions.BadRequestException;
import com.cooksys.social_media_demo.exceptions.NotFoundException;

import com.cooksys.social_media_demo.mappers.HashtagMapper;
import com.cooksys.social_media_demo.mappers.TweetMapper;
import com.cooksys.social_media_demo.repositories.HashtagRepository;
import com.cooksys.social_media_demo.repositories.TweetRepository;
import com.cooksys.social_media_demo.services.HashtagService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService {

    private final HashtagRepository hashtagRepository;

    private final HashtagMapper hashtagMapper;

    private final  TweetRepository tweetRepository;
    private final TweetMapper tweetMapper;

    private void checkLabelNullValues(String string){
        Pattern pattern = Pattern.compile("\\s+");

        if (string.equals("")) {
            throw new BadRequestException("You must provide a hashtag label.");
        } else if (string.matches(String.valueOf(pattern))) {
            throw new BadRequestException("You must provide a label.");
        }
    }

    @Override
    public List<TweetResponseDto> getAllTweetsByTag(String label) {
        checkLabelNullValues(label);
        Optional<Hashtag> hashTagToFind = hashtagRepository.findByLabel(label);

        if (hashTagToFind.isEmpty()){
            throw new NotFoundException("There is no hashtag with this label " + label + ".");
        } else {
            Hashtag hashtag = hashTagToFind.get();
            //<List<Tweet>> optionalTweetList = hashtagRepository.findAllTweetsByLabel(hashtag.getLabel());
            Optional<List<Tweet>> optionalTweetList = tweetRepository.findAllTweetsByHashtagsLabel(hashtag.getLabel());
            if(optionalTweetList.isEmpty()){
                throw new NotFoundException("There are no tweets with this Hashtag label: " + label + ".");
            } else {
                return tweetMapper.entitiesToDtos(optionalTweetList.get());
            }
        }
    }
    
    @Override
	public List<HashtagDto> getTags() {
		return hashtagMapper.entitiesToDto(hashtagRepository.findAll());
	}
}
