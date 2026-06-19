package com.project.fitness.service;

import com.project.fitness.dto.RegisterRequest;
import com.project.fitness.dto.UserResponse;
import com.project.fitness.model.User;
import com.project.fitness.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final ModelMapper mapper;

    public UserResponse register(RegisterRequest registerRequest) {
        User savedUser = mapper.map(registerRequest,User.class);
        savedUser = userRepository.save(savedUser);
        return mapper.map(savedUser,UserResponse.class);

    }
}
