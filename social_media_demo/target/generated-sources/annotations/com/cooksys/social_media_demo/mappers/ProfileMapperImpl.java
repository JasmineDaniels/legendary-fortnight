package com.cooksys.social_media_demo.mappers;

import com.cooksys.social_media_demo.dtos.ProfileDto;
import com.cooksys.social_media_demo.entities.Profile;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-04-04T11:22:29-0400",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 19.0.1 (Oracle Corporation)"
)
@Component
public class ProfileMapperImpl implements ProfileMapper {

    @Override
    public Profile dtoToEntity(ProfileDto profileDto) {
        if ( profileDto == null ) {
            return null;
        }

        Profile profile = new Profile();

        profile.setFirstName( profileDto.getFirstName() );
        profile.setLastName( profileDto.getLastName() );
        profile.setEmail( profileDto.getEmail() );
        profile.setPhone( profileDto.getPhone() );

        return profile;
    }
}
