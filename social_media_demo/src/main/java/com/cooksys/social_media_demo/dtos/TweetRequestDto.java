package com.cooksys.social_media_demo.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class TweetRequestDto {
    private String content;
    private CredentialsDto credentials; // user info will be stored here
}
