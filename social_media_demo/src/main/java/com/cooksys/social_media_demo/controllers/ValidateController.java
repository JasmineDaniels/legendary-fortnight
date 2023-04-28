package com.cooksys.social_media_demo.controllers;

import com.cooksys.social_media_demo.services.ValidateService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/validate")
@RequiredArgsConstructor
public class ValidateController {

    private final ValidateService validateService;

    @GetMapping("/tag/exists/{label}")
    public boolean validateTagExists(@PathVariable String label) {
        return validateService.validateTagExists(label);
    }
	
	@GetMapping("/username/exists/@{username}")
	public boolean validateUsernameExists(@PathVariable String username) {
		return validateService.validateUsernameExists(username);
	}

    @GetMapping("/username/available/@{username}")
    public Boolean validateUsernameAvailable(@PathVariable String username){
        return validateService.validateUsernameAvailable(username);
    }

}
