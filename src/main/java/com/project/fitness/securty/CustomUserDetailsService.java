package com.project.fitness.securty;

import com.project.fitness.model.User;
import com.project.fitness.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.mapping.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {


    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String id)
            throws UsernameNotFoundException {


        User user = userRepository.findById(id)
                .or(() -> userRepository.findByEmail(id))  // fallback
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + id));

        return org.springframework.security.core.userdetails.User
                .builder()
                .username(user.getId())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();

    }
}