package com.project.CoursePlatform.service;

import com.project.CoursePlatform.Dto.AuthResponseDto;
import com.project.CoursePlatform.Dto.LoginRequestDto;
import com.project.CoursePlatform.Dto.RegisterRequestDto;
import com.project.CoursePlatform.entity.User;
import com.project.CoursePlatform.exception.ConflictException;
import com.project.CoursePlatform.repository.UserRepository;
import com.project.CoursePlatform.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    UserRepository userRepository;

    private PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    public void register(RegisterRequestDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Email Already Exists");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);
    }

    public AuthResponseDto login(LoginRequestDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        String token = jwtUtil.generateToken(request.getEmail());
        return new AuthResponseDto(token, request.getEmail(), 86400);
    }
}
