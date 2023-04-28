package com.cooksys.social_media_demo.mappers;


import com.cooksys.social_media_demo.entities.Hashtag;

import java.util.List;


import org.mapstruct.Mapper;

import com.cooksys.social_media_demo.dtos.HashtagDto;


@Mapper(componentModel = "spring")
public interface HashtagMapper {

	List<HashtagDto> entitiesToDto(List<Hashtag> hashtags);

    Hashtag labelToEntity(String label);

}
