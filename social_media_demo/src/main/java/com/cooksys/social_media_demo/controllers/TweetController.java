package com.cooksys.social_media_demo.controllers;
import java.util.List;


import com.cooksys.social_media_demo.dtos.CredentialsDto;
import com.cooksys.social_media_demo.dtos.TweetRequestDto;
import com.cooksys.social_media_demo.dtos.TweetResponseDto;
import com.cooksys.social_media_demo.services.TweetService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cooksys.social_media_demo.dtos.ContextDto;
import com.cooksys.social_media_demo.dtos.HashtagDto;
import com.cooksys.social_media_demo.dtos.UserResponseDto;


import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tweets")
@RequiredArgsConstructor
public class TweetController {

    private final TweetService tweetService;

    @GetMapping
    public List<TweetResponseDto> getAllTweets() {
        return tweetService.getAllTweets();
    }

    @PostMapping
    public TweetResponseDto createTweet(@RequestBody TweetRequestDto tweetRequestDto) {
        return tweetService.createTweet(tweetRequestDto);
    }

    @GetMapping("/{id}")
    public TweetResponseDto getTweetById(@PathVariable Long id) {
        return tweetService.getTweetById(id);
    }

    @DeleteMapping("/{id}")
    public TweetResponseDto deleteTweetById(@PathVariable Long id, @RequestBody CredentialsDto credentialsDto) {
        return tweetService.deleteTweetById(id, credentialsDto);
    }

    @PostMapping("/{id}/like")
    public void likeTweetById(@PathVariable Long id, @RequestBody CredentialsDto credentialsDto) {
        tweetService.likeTweetById(id, credentialsDto);
    }
	
	@GetMapping("/{id}/context")
	public ContextDto getTweetContextFromId(@PathVariable Long id) {
		return tweetService.getTweetContextFromId(id);
	}
	
	@PostMapping("/{id}/repost")
	public TweetResponseDto createRepost(@PathVariable Long id, @RequestBody CredentialsDto credentialsDto) {
		return tweetService.createRepost(id, credentialsDto);
	}
	
	@GetMapping("/{id}/tags")
	public List<HashtagDto> getTagsById(@PathVariable Long id) {
		return tweetService.getTagsById(id);
	}
	
	@GetMapping("/{id}/likes")
	public List<UserResponseDto> getLikes(@PathVariable Long id) {
		return tweetService.getLikes(id);
	}

    @PostMapping("/{id}/reply")
    public TweetResponseDto postReplyTweet(@PathVariable Long id, @RequestBody TweetRequestDto tweetRequestDto){
        return tweetService.postReplyTweet(id, tweetRequestDto);
    }

    @GetMapping("/{id}/replies")
    public List<TweetResponseDto> getRepliesById(@PathVariable Long id) {
        return tweetService.getRepliesById(id);
    }

    @GetMapping("/{id}/reposts")
    public List<TweetResponseDto> getReposts(@PathVariable Long id){
        return tweetService.getReposts(id);
    }

    @GetMapping("/{id}/mentions")
    public List<UserResponseDto> getUsersMentioned(@PathVariable Long id){
        return tweetService.getUsersMentioned(id);
    }

}