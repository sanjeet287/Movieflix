package com.movieflix.userservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.movieflix.userservice.dto.UpdateUserRequest;
import com.movieflix.userservice.dto.UserResponse;
import com.movieflix.userservice.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{email}")
    public ResponseEntity<UserResponse> getUser(@PathVariable String email) {
    	UserResponse user = userService.getUserByEmail(email);
    	System.out.println("getting user from user service: "+user);
        return ResponseEntity.ok(user);
    }

    @PutMapping("update/{email}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable String email,
                                           @RequestBody @Valid UpdateUserRequest request) {
        return ResponseEntity.ok(userService.updateUser(email, request));
    }

    @DeleteMapping("delete/{email}")
    public ResponseEntity<Void> deleteUser(@PathVariable String email) {
        userService.deleteUser(email);
        return ResponseEntity.noContent().build();
    }
}

