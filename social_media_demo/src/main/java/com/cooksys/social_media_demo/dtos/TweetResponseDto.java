package com.cooksys.social_media_demo.dtos;

import java.sql.Timestamp;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class TweetResponseDto {
    private Long id;

    private Timestamp posted;

    private UserResponseDto author;

    private String content;

    private TweetResponseDto inReplyTo; //TweetResponseDto

    private TweetResponseDto repostOf; //TweetResponseDto
}