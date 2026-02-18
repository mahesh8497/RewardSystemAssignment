package com.rewardSystem.dto;

import com.rewardSystem.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private String confirmPassword;
    private String role; // Optional - defaults to ROLE_USER if not provided

    /**
     * Validates registration request.
     *
     * @return error message if invalid, null if valid
     */
    public String validate() {
        if (username == null || username.trim().isEmpty()) {
            return "Username is required";
        }
        if (username.length() < 3) {
            return "Username must be at least 3 characters";
        }
        if (email == null || email.trim().isEmpty()) {
            return "Email is required";
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return "Email is invalid";
        }
        if (password == null || password.trim().isEmpty()) {
            return "Password is required";
        }
        if (password.length() < 6) {
            return "Password must be at least 6 characters";
        }
        if (!password.equals(confirmPassword)) {
            return "Passwords do not match";
        }
        return null;
    }
}

