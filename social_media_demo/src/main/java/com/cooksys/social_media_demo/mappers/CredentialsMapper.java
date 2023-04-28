package com.cooksys.social_media_demo.mappers;

import com.cooksys.social_media_demo.dtos.CredentialsDto;
import com.cooksys.social_media_demo.entities.Credentials;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CredentialsMapper {

	Credentials dtoToEntity(CredentialsDto credentialsDto);

}
