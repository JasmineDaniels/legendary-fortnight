package com.cooksys.social_media_demo.utilities;


import com.cooksys.social_media_demo.dtos.CredentialsDto;
import com.cooksys.social_media_demo.dtos.ProfileDto;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.cooksys.social_media_demo.entities.Tweet;
import com.cooksys.social_media_demo.entities.User;
import com.cooksys.social_media_demo.exceptions.BadRequestException;
import com.cooksys.social_media_demo.exceptions.NotAuthorizedException;
import com.cooksys.social_media_demo.exceptions.NotFoundException;
import com.cooksys.social_media_demo.repositories.UserRepository;

import com.cooksys.social_media_demo.repositories.TweetRepository;
import com.cooksys.social_media_demo.repositories.UserRepository;


@Component
public class Utilities {

    private final UserRepository userRepository;
    private final TweetRepository tweetRepository;

    public Utilities(UserRepository userRepository, TweetRepository tweetRepository) {
        this.userRepository = userRepository;
        this.tweetRepository = tweetRepository;
    }

    public void checkCredentials(CredentialsDto credentialsDto) {
        //Check credentials
        if (credentialsDto != null) {
            if (credentialsDto.getUsername() != null) {
                if (credentialsDto.getUsername().equals("")) {
                    throw new BadRequestException("Username cannot be empty");
                }
            } else {
                throw new BadRequestException("Username cannot be null");
            }
            if (credentialsDto.getPassword() != null) {
                if (credentialsDto.getPassword().equals("")) {
                    throw new BadRequestException("Password cannot be empty");
                }
            } else {
                throw new BadRequestException("Password cannot be null");
            }
        } else {
            throw new BadRequestException("Credentials cannot be null");
        }
    }


    public void checkIsActiveUser(CredentialsDto credentialsDto) {
        //Check database
        Optional<User> existingUser = userRepository.findByCredentials_Username(credentialsDto.getUsername());

        //Determine if active user
        if (existingUser.isEmpty()) {
            throw new NotFoundException("Error: User with username @" + credentialsDto.getUsername() + " does not exist.");
        } else {
            if (existingUser.get().isDeleted()) {
                throw new NotFoundException("Error: User with username @" + credentialsDto.getUsername() + " does not exist.");
            }
            if(!existingUser.get().getCredentials().getPassword().equals(credentialsDto.getPassword())) {
                throw new NotAuthorizedException("Password is incorrect. Please try again.");
            }
        }
    }

    public void checkProfile(ProfileDto profileDto) {
        //Checks Profile first for missing fields
        if (profileDto != null) {
            if (profileDto.getEmail() == null) {
                throw new BadRequestException("Email cannot be null");
            } else {
                if (profileDto.getEmail().equals("")) {
                    throw new BadRequestException("Email cannot be empty");
                }
            }
        } else {
            throw new BadRequestException("Profile cannot be null");
        }
    }
    
    public void checkUserByUsername(String username) {
    	Optional<User> userToFind = userRepository.findByCredentials_Username(username);
    	if (userToFind.isEmpty()) {
    		throw new NotFoundException("Error: User with username @" + username + " does not exist.");
    	}
    	if (userToFind.get().isDeleted()) {
    		throw new NotFoundException("Error: User with username @" + username + " does not exist.");
    	}
    }
    
    public void checkIsActiveTweet(Long id) {
    	Optional<Tweet> tweet = tweetRepository.findById(id);
    	if (tweet.isEmpty()) {
    		throw new NotFoundException("Error: Tweet with id " + id + " does not exist.");
    	}
    	if (tweet.get().isDeleted()) {
    		throw new NotFoundException("Error: Tweet with id " + id + " does not exist.");
    	}
    
    }

    public void checkUsernameNullValues(String username){
        if (username == null || username.equals("")) {
            throw new BadRequestException("Username cannot be missing or empty");
        }
    }

}
