package com.movieflix.userservice.service;

import com.movieflix.userservice.dto.LoginRequest;
import com.movieflix.userservice.dto.SignupRequest;
import com.movieflix.userservice.dto.UpdateUserRequest;
import com.movieflix.userservice.dto.UserResponse;

public interface UserService {

	UserResponse registerUser(SignupRequest request);

	String loginUser(LoginRequest request);

	UserResponse getUserByEmail(String email);

	UserResponse updateUser(String email, UpdateUserRequest request);

	void deleteUser(String email);
}
