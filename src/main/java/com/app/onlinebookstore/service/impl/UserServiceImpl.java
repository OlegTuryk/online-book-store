package com.app.onlinebookstore.service.impl;

import com.app.onlinebookstore.dto.user.UserRegistrationRequestDto;
import com.app.onlinebookstore.dto.user.UserResponseDto;
import com.app.onlinebookstore.exception.EntityNotFoundException;
import com.app.onlinebookstore.exception.RegistrationException;
import com.app.onlinebookstore.mapper.UserMapper;
import com.app.onlinebookstore.model.Role;
import com.app.onlinebookstore.model.User;
import com.app.onlinebookstore.repository.RoleRepository;
import com.app.onlinebookstore.repository.UserRepository;
import com.app.onlinebookstore.service.UserService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto userRegistrationRequestDto)
            throws RegistrationException {
        if (userRepository.findByEmail(userRegistrationRequestDto.getEmail()).isPresent()) {
            throw new RegistrationException("Unable to complete registration");
        }
        User user = userMapper.toModel(userRegistrationRequestDto);
        Role role = roleRepository.getByName(Role.RoleName.ROLE_USER);
        user.setRoles(Set.of(role));
        user.setPassword(passwordEncoder.encode(userRegistrationRequestDto.getPassword()));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public User getUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(username).orElseThrow(
                () -> new EntityNotFoundException("Can't find user by username: "
                        + username));
    }
}
