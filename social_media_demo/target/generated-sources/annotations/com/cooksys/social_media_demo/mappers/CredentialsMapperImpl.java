package com.cooksys.social_media_demo.mappers;

import com.cooksys.social_media_demo.dtos.CredentialsDto;
import com.cooksys.social_media_demo.entities.Credentials;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-04-04T11:22:29-0400",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 19.0.1 (Oracle Corporation)"
)
@Component
public class CredentialsMapperImpl implements CredentialsMapper {

    @Override
    public Credentials dtoToEntity(CredentialsDto credentialsDto) {
        if ( credentialsDto == null ) {
            return null;
        }

        Credentials credentials = new Credentials();

        credentials.setUsername( credentialsDto.getUsername() );
        credentials.setPassword( credentialsDto.getPassword() );

        return credentials;
    }
}
