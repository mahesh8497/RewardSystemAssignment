package com.rewardSystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User entity representing system users with authentication and authorization.
 * Supports role-based access control (RBAC).
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private Boolean enabled = true;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(name = "created_at")
    private Long createdAt;

    @Column(name = "last_login")
    private Long lastLogin;

    /**
     * Enum for user roles with authority levels.
     */
    public enum UserRole {
        ROLE_ADMIN,      // Full system access
        ROLE_MANAGER,    // Can view and manage customer rewards
        ROLE_USER        // Can only view own rewards
    }

    @PrePersist
    protected void onCreate() {
        createdAt = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", enabled=" + enabled +
                ", role=" + role +
                ", createdAt=" + createdAt +
                '}';
    }
}

