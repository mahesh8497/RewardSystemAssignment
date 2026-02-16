package com.rewardSystem.controller;

import com.rewardSystem.dto.ErrorResponse;
import com.rewardSystem.dto.LoginRequest;
import com.rewardSystem.dto.LoginResponse;
import com.rewardSystem.dto.RegisterRequest;
import com.rewardSystem.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for authentication and authorization endpoints.
 * Provides login, registration, and token validation functionality.
 */
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private AuthenticationService authenticationService;

    /**
     * User login endpoint.
     * Authenticates user credentials and returns JWT token.
     *
     * @param loginRequest login credentials
     * @return LoginResponse with JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        logger.info("Login request received for user: {}", loginRequest.getUsername());

        try {
            if (loginRequest.getUsername() == null || loginRequest.getUsername().trim().isEmpty()) {
                logger.warn("Login request with empty username");
                return ResponseEntity.badRequest().body(
                        new ErrorResponse(400, "BAD_REQUEST", "Username is required", "/auth/login")
                );
            }

            if (loginRequest.getPassword() == null || loginRequest.getPassword().trim().isEmpty()) {
                logger.warn("Login request with empty password");
                return ResponseEntity.badRequest().body(
                        new ErrorResponse(400, "BAD_REQUEST", "Password is required", "/auth/login")
                );
            }

            LoginResponse response = authenticationService.login(loginRequest);
            logger.info("Login successful for user: {}", loginRequest.getUsername());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Login failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new ErrorResponse(401, "UNAUTHORIZED", e.getMessage(), "/auth/login")
            );
        }
    }

    /**
     * User registration endpoint.
     * Creates a new user account and returns JWT token.
     *
     * @param registerRequest registration details
     * @return LoginResponse with JWT token
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        logger.info("Registration request received for user: {}", registerRequest.getUsername());

        try {
            LoginResponse response = authenticationService.register(registerRequest);
            logger.info("Registration successful for user: {}", registerRequest.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            logger.error("Registration failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(
                    new ErrorResponse(400, "BAD_REQUEST", e.getMessage(), "/auth/register")
            );
        }
    }

    /**
     * Token validation endpoint.
     * Validates JWT token and returns validation result.
     *
     * @param token the JWT token to validate
     * @return validation result
     */
    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestParam String token) {
        logger.trace("Token validation request received");

        try {
            if (token == null || token.trim().isEmpty()) {
                logger.warn("Validation request with empty token");
                return ResponseEntity.badRequest().body(
                        new ErrorResponse(400, "BAD_REQUEST", "Token is required", "/auth/validate")
                );
            }

            boolean isValid = authenticationService.validateToken(token);

            if (isValid) {
                String username = authenticationService.getUsernameFromToken(token);
                logger.debug("Token validated successfully for user: {}", username);
                return ResponseEntity.ok().body(new ValidationResponse(true, "Token is valid", username));
            } else {
                logger.warn("Token validation failed - invalid token");
                return ResponseEntity.ok().body(new ValidationResponse(false, "Token is invalid", null));
            }

        } catch (Exception e) {
            logger.error("Token validation error: {}", e.getMessage());
            return ResponseEntity.ok().body(new ValidationResponse(false, "Token validation failed: " + e.getMessage(), null));
        }
    }

    /**
     * Inner class for token validation response.
     */
    public static class ValidationResponse {
        public boolean valid;
        public String message;
        public String username;

        public ValidationResponse(boolean valid, String message, String username) {
            this.valid = valid;
            this.message = message;
            this.username = username;
        }
    }
}

