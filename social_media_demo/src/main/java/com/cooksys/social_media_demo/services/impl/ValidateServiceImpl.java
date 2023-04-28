package com.cooksys.social_media_demo.services.impl;


import com.cooksys.social_media_demo.entities.Hashtag;

import com.cooksys.social_media_demo.repositories.HashtagRepository;
import com.cooksys.social_media_demo.services.ValidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


import com.cooksys.social_media_demo.entities.User;

import com.cooksys.social_media_demo.repositories.UserRepository;

import com.cooksys.social_media_demo.utilities.Utilities;


@Service
@RequiredArgsConstructor
public class ValidateServiceImpl implements ValidateService {

    private final UserRepository userRepository;

    private final HashtagRepository hashtagRepository;

    private final Utilities utilities;

    @Override
    public boolean validateTagExists(String label) {
        utilities.checkUsernameNullValues(label);
        Optional<Hashtag> tag = hashtagRepository.findByLabel(label);
        return tag.isPresent();
    }
	
	@Override
	public boolean validateUsernameExists(String username) {
        utilities.checkUsernameNullValues(username);
		Optional<User> possibleUser = userRepository.findByCredentials_Username(username);
		return possibleUser.isPresent();
	}

    @Override
    public Boolean validateUsernameAvailable(String username) {
        utilities.checkUsernameNullValues(username);
        utilities.checkUsernameNullValues(username);
        return userRepository.findByCredentials_Username(username).isEmpty();
    }

}
