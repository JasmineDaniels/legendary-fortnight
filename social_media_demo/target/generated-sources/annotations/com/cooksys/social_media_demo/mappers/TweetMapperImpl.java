package com.cooksys.social_media_demo.mappers;

import com.cooksys.social_media_demo.dtos.CredentialsDto;
import com.cooksys.social_media_demo.dtos.TweetRequestDto;
import com.cooksys.social_media_demo.dtos.TweetResponseDto;
import com.cooksys.social_media_demo.entities.Tweet;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-04-04T11:22:29-0400",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 19.0.1 (Oracle Corporation)"
)
@Component
public class TweetMapperImpl implements TweetMapper {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Tweet requestDtoToEntity(TweetRequestDto tweetRequestDto) {
        if ( tweetRequestDto == null ) {
            return null;
        }

        Tweet tweet = new Tweet();

        tweet.setContent( tweetRequestDto.getContent() );

        return tweet;
    }

    @Override
    public List<TweetResponseDto> entitiesToResponseDtos(List<Tweet> activeMentions) {
        if ( activeMentions == null ) {
            return null;
        }

        List<TweetResponseDto> list = new ArrayList<TweetResponseDto>( activeMentions.size() );
        for ( Tweet tweet : activeMentions ) {
            list.add( entityToResponseDto( tweet ) );
        }

        return list;
    }

    @Override
    public TweetResponseDto entityToResponseDto(Tweet tweet) {
        if ( tweet == null ) {
            return null;
        }

        TweetResponseDto tweetResponseDto = new TweetResponseDto();

        tweetResponseDto.setId( tweet.getId() );
        tweetResponseDto.setPosted( tweet.getPosted() );
        tweetResponseDto.setAuthor( userMapper.entityToResponseDto( tweet.getAuthor() ) );
        tweetResponseDto.setContent( tweet.getContent() );
        tweetResponseDto.setInReplyTo( entityToResponseDto( tweet.getInReplyTo() ) );
        tweetResponseDto.setRepostOf( entityToResponseDto( tweet.getRepostOf() ) );

        return tweetResponseDto;
    }

    @Override
    public List<TweetResponseDto> entitiesToDto(List<Tweet> tweets) {
        if ( tweets == null ) {
            return null;
        }

        List<TweetResponseDto> list = new ArrayList<TweetResponseDto>( tweets.size() );
        for ( Tweet tweet : tweets ) {
            list.add( entityToResponseDto( tweet ) );
        }

        return list;
    }

    @Override
    public Tweet credentialsDtoToEntity(CredentialsDto credentialsDto) {
        if ( credentialsDto == null ) {
            return null;
        }

        Tweet tweet = new Tweet();

        return tweet;
    }

    @Override
    public List<TweetResponseDto> entitiesToDtos(List<Tweet> tweetList) {
        if ( tweetList == null ) {
            return null;
        }

        List<TweetResponseDto> list = new ArrayList<TweetResponseDto>( tweetList.size() );
        for ( Tweet tweet : tweetList ) {
            list.add( entityToResponseDto( tweet ) );
        }

        return list;
    }
}
