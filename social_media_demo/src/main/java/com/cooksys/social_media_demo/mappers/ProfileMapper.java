package com.cooksys.social_media_demo.mappers;

import com.cooksys.social_media_demo.dtos.ProfileDto;
import com.cooksys.social_media_demo.entities.Profile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    Profile dtoToEntity(ProfileDto profileDto);

}
