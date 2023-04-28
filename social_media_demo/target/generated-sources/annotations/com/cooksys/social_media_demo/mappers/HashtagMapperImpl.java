package com.cooksys.social_media_demo.mappers;

import com.cooksys.social_media_demo.dtos.HashtagDto;
import com.cooksys.social_media_demo.entities.Hashtag;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-04-04T11:22:28-0400",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 19.0.1 (Oracle Corporation)"
)
@Component
public class HashtagMapperImpl implements HashtagMapper {

    @Override
    public List<HashtagDto> entitiesToDto(List<Hashtag> hashtags) {
        if ( hashtags == null ) {
            return null;
        }

        List<HashtagDto> list = new ArrayList<HashtagDto>( hashtags.size() );
        for ( Hashtag hashtag : hashtags ) {
            list.add( hashtagToHashtagDto( hashtag ) );
        }

        return list;
    }

    @Override
    public Hashtag labelToEntity(String label) {
        if ( label == null ) {
            return null;
        }

        Hashtag hashtag = new Hashtag();

        hashtag.setLabel( label );

        return hashtag;
    }

    protected HashtagDto hashtagToHashtagDto(Hashtag hashtag) {
        if ( hashtag == null ) {
            return null;
        }

        HashtagDto hashtagDto = new HashtagDto();

        hashtagDto.setLabel( hashtag.getLabel() );
        hashtagDto.setFirstUsed( hashtag.getFirstUsed() );
        hashtagDto.setLastUsed( hashtag.getLastUsed() );

        return hashtagDto;
    }
}
