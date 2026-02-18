package com.rewardSystem.service;

import com.rewardSystem.dto.LoginRequest;
import com.rewardSystem.dto.LoginResponse;
import com.rewardSystem.dto.RegisterRequest;
import com.rewardSystem.entity.User;
import com.rewardSystem.exception.DataProcessingException;
import com.rewardSystem.repository.UserRepository;
import com.rewardSystem.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Value("${jwt.expiration:86400000}")
    private int jwtExpirationMs;


    public LoginResponse login(LoginRequest loginRequest) throws AuthenticationException {
        logger.info("Attempting login for user: {}", loginRequest.getUsername());

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            String token = jwtTokenProvider.generateToken(authentication);
            User user = userRepository.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Update last login timestamp
            user.setLastLogin(System.currentTimeMillis());
            userRepository.save(user);

            logger.info("User logged in successfully: {}", loginRequest.getUsername());

            return new LoginResponse(token, user.getUsername(), user.getRole().toString(), jwtExpirationMs);

        } catch (AuthenticationException e) {
            logger.error("Authentication failed for user: {}", loginRequest.getUsername());
            throw new DataProcessingException("Invalid username or password");
        }
    }
    public LoginResponse register(RegisterRequest registerRequest) throws DataProcessingException {
        logger.info("Attempting registration for user: {}", registerRequest.getUsername());

        // Validate request
        String validationError = registerRequest.validate();
        if (validationError != null) {
            logger.warn("Registration validation failed: {}", validationError);
            throw new DataProcessingException(validationError);
        }

        // Check if user already exists
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            logger.warn("Username already exists: {}", registerRequest.getUsername());
            throw new DataProcessingException("Username already exists");
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            logger.warn("Email already exists: {}", registerRequest.getEmail());
            throw new DataProcessingException("Email already exists");
        }

        try {
            // Create new user
            User user = new User();
            user.setUsername(registerRequest.getUsername());
            user.setEmail(registerRequest.getEmail());
            user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            user.setEnabled(true);

            // Set role - default to ROLE_USER if not provided
            String roleStr = registerRequest.getRole() != null ? registerRequest.getRole() : "ROLE_USER";
            user.setRole(User.UserRole.valueOf(roleStr));

            User savedUser = userRepository.save(user);

            logger.info("User registered successfully: {}", registerRequest.getUsername());

            // Generate token for newly registered user
            String token = jwtTokenProvider.generateTokenFromUsername(savedUser.getUsername());

            return new LoginResponse(token, savedUser.getUsername(), savedUser.getRole().toString(),
                    jwtExpirationMs, "User registered successfully");

        } catch (IllegalArgumentException e) {
            logger.error("Invalid role provided: {}", registerRequest.getRole());
            throw new DataProcessingException("Invalid role: " + registerRequest.getRole());
        } catch (Exception e) {
            logger.error("Registration failed: {}", e.getMessage());
            throw new DataProcessingException("Registration failed: " + e.getMessage());
        }
    }

    public boolean validateToken(String token) {
        logger.trace("Validating token");
        return jwtTokenProvider.validateToken(token);
    }

    public String getUsernameFromToken(String token) {
        logger.trace("Extracting username from token");
        return jwtTokenProvider.getUsernameFromToken(token);
    }
}

