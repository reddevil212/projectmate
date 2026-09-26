package com.proj.mate.controller;


import com.proj.mate.dto.AuthRequestDto;
import com.proj.mate.dto.RefreshTokenRequestDto;
import com.proj.mate.dto.RegisterRequestDto;
import com.proj.mate.dto.TokenResponseDto;
import com.proj.mate.service.AuthService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth/v1")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody AuthRequestDto requestDto) {
        TokenResponseDto tokenResponse = authService.login(requestDto);
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public TokenResponseDto register(@RequestBody RegisterRequestDto requestDto) {
        return authService.register(requestDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody Long userId) {
        authService.logout(userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDto> refreshToken(@RequestBody RefreshTokenRequestDto requestDto) {
        TokenResponseDto tokenResponse = authService.refreshToken(requestDto);
        return ResponseEntity.ok(tokenResponse);
    }
}
