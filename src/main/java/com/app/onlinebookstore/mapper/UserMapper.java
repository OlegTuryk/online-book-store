package com.app.onlinebookstore.mapper;

import com.app.onlinebookstore.config.MapperConfig;
import com.app.onlinebookstore.dto.user.UserRegistrationRequestDto;
import com.app.onlinebookstore.dto.user.UserResponseDto;
import com.app.onlinebookstore.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toUserResponse(User user);

    User toModel(UserRegistrationRequestDto requestDto);
}
