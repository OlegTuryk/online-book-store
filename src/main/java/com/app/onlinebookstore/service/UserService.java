package com.app.onlinebookstore.service;

import com.app.onlinebookstore.dto.user.UserRegistrationRequestDto;
import com.app.onlinebookstore.dto.user.UserResponseDto;
import com.app.onlinebookstore.exception.RegistrationException;
import com.app.onlinebookstore.model.User;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto userRegistrationRequestDto)
            throws RegistrationException;

    User getUser();
}
