package org.roadmapBack.controller;

import org.roadmapBack.dto.AuthResponseDto;
import org.roadmapBack.dto.LoginRequestDto;
import org.roadmapBack.service.AuthService;
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
    public AuthResponseDto login(@RequestBody LoginRequestDto request){
        return authService.login(request);
    }

    @PostMapping("/refresh")
    public AuthResponseDto refresh(@RequestBody String refreshToken){
        return authService.refresh(refreshToken);
    }
}
