package com.cooksys.social_media_demo.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class NotFoundException extends RuntimeException{

    private String message;
}
