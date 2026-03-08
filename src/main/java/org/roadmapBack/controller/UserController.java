package org.roadmapBack.controller;

import jakarta.validation.Valid;
import org.roadmapBack.dto.RegisterRequestDto;
import org.roadmapBack.dto.UserDto;
import org.roadmapBack.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody RegisterRequestDto request){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(request));
    }
}
