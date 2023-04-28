package com.cooksys.social_media_demo.controllers;


import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cooksys.social_media_demo.dtos.HashtagDto;
import com.cooksys.social_media_demo.services.HashtagService;
import lombok.RequiredArgsConstructor;
import com.cooksys.social_media_demo.dtos.TweetResponseDto;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
public class HashtagController {

	
	private final HashtagService hashtagService;
	
	@GetMapping
	public List<HashtagDto> getTags() {
		return hashtagService.getTags();
	}

    @GetMapping("/{label}")
    public List<TweetResponseDto> getAllTweetsByTag(@PathVariable String label){
        return hashtagService.getAllTweetsByTag(label);
    }

}
