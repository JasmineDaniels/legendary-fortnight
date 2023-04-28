package com.cooksys.social_media_demo.services;

public interface ValidateService {

    boolean validateTagExists(String label);

	boolean validateUsernameExists(String username);

    Boolean validateUsernameAvailable(String username);

}
