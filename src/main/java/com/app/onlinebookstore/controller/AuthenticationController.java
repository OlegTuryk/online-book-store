package com.app.onlinebookstore.controller;

import com.app.onlinebookstore.dto.user.UserLoginRequestDto;
import com.app.onlinebookstore.dto.user.UserLoginResponseDto;
import com.app.onlinebookstore.dto.user.UserRegistrationRequestDto;
import com.app.onlinebookstore.dto.user.UserResponseDto;
import com.app.onlinebookstore.exception.RegistrationException;
import com.app.onlinebookstore.security.AuthenticationService;
import com.app.onlinebookstore.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authenticate management", description = "Endpoints for managing authentication")
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @Operation(summary = "User Login",
            description = "Authenticate a user with their login credentials")
    @PostMapping("/login")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto userLoginRequestDto) {
        return authenticationService.authenticate(userLoginRequestDto);
    }

    @Operation(summary = "User Registration",
            description = "Register a new user with their provided details")
    @PostMapping("/register")
    public UserResponseDto register(
            @RequestBody @Valid UserRegistrationRequestDto userRegistrationRequestDto)
            throws RegistrationException {
        return userService.register(userRegistrationRequestDto);
    }
}
