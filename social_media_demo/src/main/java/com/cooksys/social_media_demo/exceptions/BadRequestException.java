package com.cooksys.social_media_demo.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class BadRequestException extends RuntimeException{

    private String message;
}
