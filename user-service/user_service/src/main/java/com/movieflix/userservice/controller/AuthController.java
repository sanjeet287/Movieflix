package com.movieflix.userservice.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.movieflix.userservice.dto.LoginRequest;
import com.movieflix.userservice.dto.SignupRequest;
import com.movieflix.userservice.dto.UserResponse;
import com.movieflix.userservice.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(@Valid @RequestBody SignupRequest request) {
    	 log.info("Received signup request for email: {}", request.getEmail());
    	UserResponse response = userService.registerUser(request);
    	log.info("Signup successful for email: {}", request.getEmail());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginRequest request) {
    	log.info("Login attempt for email: {}", request.getEmail());
        String jwtToken = userService.loginUser(request);
        log.info("Login success for email: {}", request.getEmail());
     // Create a Map to hold both the message and the token
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "Token generated successfully!");
        responseBody.put("token", jwtToken);

        return ResponseEntity.ok(responseBody);
    }

}
