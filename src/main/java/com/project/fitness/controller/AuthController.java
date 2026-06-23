package com.project.fitness.controller;

import com.project.fitness.dto.LoginRequest;
import com.project.fitness.dto.LoginResponse;
import com.project.fitness.dto.RegisterRequest;
import com.project.fitness.dto.UserResponse;
import com.project.fitness.model.User;
import com.project.fitness.repository.UserRepository;
import com.project.fitness.securty.JwtUtils;
import com.project.fitness.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final ModelMapper modelMapper;

    @PostMapping("/register")
    public UserResponse register(@RequestBody RegisterRequest registerRequest){
        return userService.register(registerRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            User user = userService.authenticate(loginRequest);

            // FIX: use user.getId() (UUID) as JWT subject, not user.getEmail()
            String token = jwtUtils.generateToken(user.getId(), user.getRole().name());

            return ResponseEntity.ok(new LoginResponse(token, modelMapper.map(user, UserResponse.class)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(401).build();
        }
    }
    }

