package com.cooksys.social_media_demo.mappers;

import com.cooksys.social_media_demo.dtos.UserRequestDto;
import com.cooksys.social_media_demo.dtos.UserResponseDto;
import com.cooksys.social_media_demo.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ProfileMapper.class, CredentialsMapper.class})
public interface UserMapper {

	User requestDtoToEntity(UserRequestDto userRequestDto);
	
	@Mapping(target="username", source="credentials.username")
	UserResponseDto entityToResponseDto(User user);

	List<UserResponseDto> entitiesToResponseDtos(List<User> users);

}
