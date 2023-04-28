package com.cooksys.social_media_demo.mappers;

import com.cooksys.social_media_demo.dtos.ProfileDto;
import com.cooksys.social_media_demo.dtos.UserRequestDto;
import com.cooksys.social_media_demo.dtos.UserResponseDto;
import com.cooksys.social_media_demo.entities.Credentials;
import com.cooksys.social_media_demo.entities.Profile;
import com.cooksys.social_media_demo.entities.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-04-04T11:22:28-0400",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 19.0.1 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Autowired
    private ProfileMapper profileMapper;
    @Autowired
    private CredentialsMapper credentialsMapper;

    @Override
    public User requestDtoToEntity(UserRequestDto userRequestDto) {
        if ( userRequestDto == null ) {
            return null;
        }

        User user = new User();

        user.setProfile( profileMapper.dtoToEntity( userRequestDto.getProfile() ) );
        user.setCredentials( credentialsMapper.dtoToEntity( userRequestDto.getCredentials() ) );

        return user;
    }

    @Override
    public UserResponseDto entityToResponseDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponseDto userResponseDto = new UserResponseDto();

        userResponseDto.setUsername( userCredentialsUsername( user ) );
        userResponseDto.setProfile( profileToProfileDto( user.getProfile() ) );
        userResponseDto.setJoined( user.getJoined() );

        return userResponseDto;
    }

    @Override
    public List<UserResponseDto> entitiesToResponseDtos(List<User> users) {
        if ( users == null ) {
            return null;
        }

        List<UserResponseDto> list = new ArrayList<UserResponseDto>( users.size() );
        for ( User user : users ) {
            list.add( entityToResponseDto( user ) );
        }

        return list;
    }

    private String userCredentialsUsername(User user) {
        if ( user == null ) {
            return null;
        }
        Credentials credentials = user.getCredentials();
        if ( credentials == null ) {
            return null;
        }
        String username = credentials.getUsername();
        if ( username == null ) {
            return null;
        }
        return username;
    }

    protected ProfileDto profileToProfileDto(Profile profile) {
        if ( profile == null ) {
            return null;
        }

        ProfileDto profileDto = new ProfileDto();

        profileDto.setFirstName( profile.getFirstName() );
        profileDto.setLastName( profile.getLastName() );
        profileDto.setEmail( profile.getEmail() );
        profileDto.setPhone( profile.getPhone() );

        return profileDto;
    }
}
