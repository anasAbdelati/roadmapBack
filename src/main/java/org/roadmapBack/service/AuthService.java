package org.roadmapBack.service;

import org.roadmapBack.config.JwtUtils;
import org.roadmapBack.dto.AuthResponseDto;
import org.roadmapBack.dto.LoginRequestDto;
import org.roadmapBack.dto.RefreshRequestDto;
import org.roadmapBack.exceptions.InvalidRefreshTokenException;
import org.roadmapBack.exceptions.WrongcredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthService(UserService userService, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    public AuthResponseDto login(LoginRequestDto request){
        final var user = userService.findUserByEmail(request.getEmail());
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new WrongcredentialsException("Email or password are not correct or account doesn't exist");
        }
        return AuthResponseDto.builder()
                .accessToken(jwtUtils.generateAccessToken(user.getEmail()))
                .refreshToken(jwtUtils.generateRefreshToken(user.getEmail()))
                .build();
    }

    public AuthResponseDto refresh(RefreshRequestDto refreshRequest){
        final var refreshToken=refreshRequest.getRefreshToken();
        if(!jwtUtils.isTokenValid(refreshToken)){
            throw new InvalidRefreshTokenException("Invalid refresh token");
        }
        final var email = jwtUtils.extractEmail(refreshToken);
        return AuthResponseDto.builder()
                .accessToken(jwtUtils.generateAccessToken(email))
                .refreshToken(refreshToken)
                .build();
    }
}