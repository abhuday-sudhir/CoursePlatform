package com.project.CoursePlatform.controller;

import com.project.CoursePlatform.Dto.AuthResponseDto;
import com.project.CoursePlatform.Dto.LoginRequestDto;
import com.project.CoursePlatform.Dto.RegisterRequestDto;
import com.project.CoursePlatform.entity.User;
import com.project.CoursePlatform.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("api/auth")
@Tag(name = "2. Sign up and Login APIs", description = "Register yourself or login using email")
public class AuthController {

    @Autowired
    AuthService authService;

    @Operation(summary = "Sign up")
    @PostMapping("register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDto request) {
        authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "message", "User registered successfully",
                        "email", request.getEmail()
                ));
    }

    @Operation(summary = "Login")
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(authService.login(request));
    }


}
