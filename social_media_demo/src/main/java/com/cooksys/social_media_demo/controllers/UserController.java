package com.cooksys.social_media_demo.controllers;

import com.cooksys.social_media_demo.dtos.CredentialsDto;
import com.cooksys.social_media_demo.dtos.TweetResponseDto;
import com.cooksys.social_media_demo.dtos.UserRequestDto;
import com.cooksys.social_media_demo.dtos.UserResponseDto;
import com.cooksys.social_media_demo.services.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
	
	private final UserService userService;
	
	@GetMapping("/@{username}")
	public UserResponseDto getUserByUsername(@PathVariable String username) {
		return userService.getUserByUsername(username);
	}
	
	@GetMapping("/@{username}/tweets")
	public List<TweetResponseDto> getTweetsByUsername(@PathVariable String username) {
		return userService.getTweetsByUsername(username);
	}

	@PatchMapping("/@{username}")
	public UserResponseDto updateUsername(@PathVariable String username, @RequestBody UserRequestDto userRequestDto) {
		return userService.updateUsername(username, userRequestDto);
	}
	
	@DeleteMapping("/@{username}")
	public UserResponseDto deleteUser(@PathVariable String username, @RequestBody CredentialsDto credentialsDto) {
		return userService.deleteUser(username, credentialsDto);
	}

    @GetMapping
    public List<UserResponseDto> getAllUsers(){ return userService.getAllUsers(); }

    @PostMapping("/@{username}/follow")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public UserResponseDto followAUser(@PathVariable String username, @RequestBody CredentialsDto credentialsDto){
        return userService.followAUser(username, credentialsDto);
    }

    @PostMapping("/@{username}/unfollow")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public UserResponseDto unfollowAUser(@PathVariable String username, @RequestBody CredentialsDto credentialsDto){
        return userService.unfollowAUser(username, credentialsDto);
    }

    @GetMapping("/@{username}/feed")
    public List<TweetResponseDto> getUsersTwitterFeed(@PathVariable String username){
        return userService.getTwitterFeed(username);
    }

	@PostMapping
    public UserResponseDto createUser(@RequestBody UserRequestDto userRequestDto) {
        return userService.createUser(userRequestDto);
    }

    @GetMapping("/@{username}/followers")
    public List<UserResponseDto> getFollowersByUsername(@PathVariable String username) {
        return userService.getFollowersByUsername(username);
    }

    @GetMapping("/@{username}/following")
    public List<UserResponseDto> getFollowingByUsername(@PathVariable String username) {
        return userService.getFollowingByUsername(username);
    }

    @GetMapping("/@{username}/mentions")
    public List<TweetResponseDto> getMentionsByUsername(@PathVariable String username) {
        return userService.getMentionsByUsername(username);
    }
}
