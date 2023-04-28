package com.cooksys.social_media_demo.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class NotAuthorizedException extends RuntimeException{

    private String message;
}
