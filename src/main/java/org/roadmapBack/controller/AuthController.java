package org.roadmapBack.controller;

import jakarta.validation.Valid;
import org.roadmapBack.dto.AuthResponseDto;
import org.roadmapBack.dto.LoginRequestDto;
import org.roadmapBack.dto.RefreshRequestDto;
import org.roadmapBack.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginRequestDto request){
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDto> refresh(@Valid @RequestBody RefreshRequestDto refreshToken){
        return ResponseEntity.ok(authService.refresh(refreshToken));
    }
}
