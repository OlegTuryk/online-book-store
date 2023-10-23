package com.app.onlinebookstore.security;

import com.app.onlinebookstore.dto.user.UserLoginRequestDto;
import com.app.onlinebookstore.dto.user.UserLoginResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthenticationService {
    private final JwtUtil jwtUtil;

    public UserLoginResponseDto authenticate(UserLoginRequestDto requestDto) {
        String token = jwtUtil.generateToken(requestDto.email());
        return new UserLoginResponseDto(token);
    }
}
