package com.project.CoursePlatform.service;

import com.project.CoursePlatform.entity.User;
import com.project.CoursePlatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {
    @Autowired
    private UserRepository userRepository;
    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found in DB: " + email));
    }

}
