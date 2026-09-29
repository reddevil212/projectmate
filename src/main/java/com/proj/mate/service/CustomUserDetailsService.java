package com.proj.mate.service;

import com.proj.mate.entity.UserInfo;
import com.proj.mate.repository.UserRepository;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        UserInfo userInfo = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found with email: " + email
                        )
                );

        String userRole = (userInfo.getRole() != null && !userInfo.getRole().trim().isEmpty())
                ? userInfo.getRole()
                : "USER";
        if (!userRole.startsWith("ROLE_")) {
            userRole = "ROLE_" + userRole;
        }

        return User.builder()
                .username(userInfo.getEmail())
                .password(userInfo.getPassword())
                .authorities(userRole)
                .build();
    }
}
