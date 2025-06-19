package com.movieflix.userservice.service;

import java.util.Set;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.movieflix.userservice.config.RabbitMQConfig;
import com.movieflix.userservice.dto.LoginRequest;
import com.movieflix.userservice.dto.SignupRequest;
import com.movieflix.userservice.dto.UpdateUserRequest;
import com.movieflix.userservice.dto.UserEvent;
import com.movieflix.userservice.dto.UserResponse;
import com.movieflix.userservice.entity.User;
import com.movieflix.userservice.repository.UserRepository;
import com.movieflix.userservice.security.JwtUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;
	private final RedisTemplate<String, Object> redisTemplate;
	private final RabbitTemplate rabbitTemplate;

	@Override
	public UserResponse registerUser(SignupRequest request) {
		log.debug("Registering new user with email: {}", request.getEmail());

		if (userRepository.existsByEmail(request.getEmail())) {
			log.warn("Attempted to register with existing email: {}", request.getEmail());
			throw new RuntimeException("Email already exists");
		}

		User user = User.builder().name(request.getName()).email(request.getEmail())
				.password(passwordEncoder.encode(request.getPassword())).phoneNumber(request.getPhoneNumber())
				.roles(Set.of("USER")).emailVerified(false).build();

		userRepository.save(user);
		log.info("User registered with ID: {}", user);

		// publish signup event
		log.info("Signing user with email pushed into Notification Queue: {}", user.getEmail());
		sendUserEvent(user, "SIGNUP");

		return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.isEmailVerified(),
				user.getPhoneNumber(), user.getProfileImageUrl());
	
	}

	@Override
	public String loginUser(LoginRequest request) {
		log.debug("Authenticating user with email: {}", request.getEmail());
		User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> {
			log.warn("No user found with email: {}", request.getEmail());
			return new RuntimeException("Invalid credentials");
		});

		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			log.warn("Password mismatch for user: {}", request.getEmail());
			throw new RuntimeException("Invalid credentials");
		}
		log.warn("Invalid credentials for {}", request.getEmail());

		String token = jwtUtil.generateToken(user);
		log.info("JWT token generated for user: {}", request.getEmail());
		// Save user info in Redis cache
		redisTemplate.opsForValue().set("user:" + user.getEmail(), user.getEmail());
		// Publish login event
		log.info("Logging user with email pushed into Notification Queue: {}", user.getEmail());
		sendUserEvent(user, "LOGIN");

		return token;
	}

	@Override
	@Cacheable(value = "users", key = "#email")
	public UserResponse getUserByEmail(String email) {
	    log.info("Fetching user from DB for email: {}", email);
	    User user = userRepository.findByEmail(email).orElseThrow(() -> {
	        log.warn("No user found with email: {}", email);
	        return new RuntimeException("Invalid credentials");
	    });
	    System.out.println("user found :"+user.getId());

	    UserResponse response = new UserResponse(
	        user.getId(),
	        user.getName(),
	        user.getEmail(),
	        user.isEmailVerified(),
	        user.getPhoneNumber(),
	        user.getProfileImageUrl()
	    );

	    log.info("Returning user response: {}", response);
	    return response;
	}


	@Override
	@CacheEvict(value = "users", key = "#email")
	public UserResponse updateUser(String email, UpdateUserRequest request) {
		User user = userRepository.findByEmail(email).orElseThrow(() -> {
			log.warn("No user found with email: {}", email);
			return new RuntimeException("Invalid credentials");
		});

		log.info("Updating user info for email: {}", email);
		user.setName(request.getName());
		user.setPhoneNumber(request.getPhoneNumber());
		User savedUser = userRepository.save(user);

		// Publish update event
		log.info("Updating user with email pushed into Notification Queue: {}", email);
		sendUserEvent(savedUser, "UPDATE_PROFILE");

		return new UserResponse(user.getId(), savedUser.getName(), user.getEmail(), user.isEmailVerified(),
				savedUser.getPhoneNumber(), user.getProfileImageUrl());
	
	}

	@Override
	@CacheEvict(value = "users", key = "#email")
	public void deleteUser(String email) {
		User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

		log.info("Deleting user with email: {}", email);
		userRepository.delete(user);
		// Publish delete event
		log.info("Deleting user with email is pushed into Notification Queue: {}", email);
		sendUserEvent(user, "DELETE");
	}

	private void sendUserEvent(User user, String eventType) {
		UserEvent event = new UserEvent(user.getId().toString(), user.getEmail(), user.getName(), eventType);

		String routingKey = switch (eventType) {
		case "SIGNUP" -> RabbitMQConfig.USER_SIGNUP_ROUTING_KEY;
		case "LOGIN" -> RabbitMQConfig.USER_LOGIN_ROUTING_KEY;
		case "UPDATE_PROFILE" -> RabbitMQConfig.USER_UPDATE_ROUTING_KEY;
		case "DELETE" -> RabbitMQConfig.USER_DELETE_ROUTING_KEY;
		default -> throw new IllegalArgumentException("Unsupported event type: " + eventType);
		};

		rabbitTemplate.convertAndSend(RabbitMQConfig.USER_EXCHANGE, routingKey, event);
	}

}
