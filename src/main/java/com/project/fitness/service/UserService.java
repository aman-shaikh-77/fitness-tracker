package com.project.fitness.service;

import com.project.fitness.dto.LoginRequest;
import com.project.fitness.dto.RegisterRequest;
import com.project.fitness.dto.UserResponse;
import com.project.fitness.model.User;
import com.project.fitness.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapper;

    public UserResponse register(RegisterRequest registerRequest) {
        User savedUser = mapper.map(registerRequest, User.class);
        savedUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        savedUser = userRepository.save(savedUser);
        return mapper.map(savedUser, UserResponse.class);
    }

    public User authenticate(LoginRequest loginRequest) {

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid Credentials"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid Credentials");
        }
        return user;
    }
}