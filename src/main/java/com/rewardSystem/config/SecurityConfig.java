//package com.rewardSystem.config;
//
//import com.rewardSystem.security.CustomUserDetailsService;
//import com.rewardSystem.security.JwtAuthenticationFilter;
//import com.rewardSystem.security.JwtTokenProvider;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
///**
// * Security Configuration for Spring Security with JWT token-based authentication.
// * Implements role-based access control and method-level security.
// */
//@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
//public class SecurityConfig {
//
//    @Autowired
//    private CustomUserDetailsService userDetailsService;
//
//    @Autowired
//    private JwtTokenProvider jwtTokenProvider;
//
//    /**
//     * Password encoder bean using BCrypt algorithm.
//     */
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    /**
//     * Authentication Manager bean.
//     */
//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
//        return http.getSharedObject(AuthenticationManagerBuilder.class)
//                .userDetailsService(userDetailsService)
//                .passwordEncoder(passwordEncoder())
//                .and()
//                .build();
//    }
//
//    /**
//     * JWT Authentication Filter bean.
//     */
//    @Bean
//    public JwtAuthenticationFilter jwtAuthenticationFilter() {
//        return new JwtAuthenticationFilter(jwtTokenProvider, userDetailsService);
//    }
//
//    /**
//     * Security Filter Chain configuration.
//     * Defines authentication, authorization, and security rules.
//     */
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .cors()
//                .and()
//                .csrf().disable()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authorizeHttpRequests((authz) -> authz
//                        // Public endpoints - no authentication required
//                        .requestMatchers("/public/**").permitAll()
//                        .requestMatchers("/auth/**").permitAll()
//                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
//
//                        // Admin endpoints - requires ADMIN role
//                        .requestMatchers("/admin/**").hasRole("ADMIN")
//
//                        // Manager endpoints - requires MANAGER or ADMIN role
//                        .requestMatchers("/manager/**").hasAnyRole("MANAGER", "ADMIN")
//
//                        // User endpoints - requires USER, MANAGER, or ADMIN role
//                        .requestMatchers("/v1/api/**").hasAnyRole("USER", "MANAGER", "ADMIN")
//
//                        // All other requests require authentication
//                        .anyRequest().authenticated()
//                )
//                .httpBasic()
//                .and()
//                .exceptionHandling()
//                .authenticationEntryPoint((request, response, authException) -> {
//                    response.sendError(401, "Unauthorized: " + authException.getMessage());
//                })
//                .accessDeniedHandler((request, response, accessDeniedException) -> {
//                    response.sendError(403, "Access Denied: " + accessDeniedException.getMessage());
//                });
//
//        // Add JWT filter
//        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//}
//


package com.rewardSystem.config;

import com.rewardSystem.security.CustomUserDetailsService;
import com.rewardSystem.security.JwtAuthenticationFilter;
import com.rewardSystem.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Password Encoder Bean
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Authentication Manager Bean
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * JWT Authentication Filter Bean
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtTokenProvider, userDetailsService);
    }

    /**
     * Security Filter Chain Configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {})
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth

                        // Public Endpoints
                        .requestMatchers("/public/**").permitAll()
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()

                        // Role-Based Endpoints
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/manager/**").hasAnyRole("MANAGER", "ADMIN")
                        .requestMatchers("/v1/api/**").hasAnyRole("USER", "MANAGER", "ADMIN")

                        // All Other Requests
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, ex) ->
                                response.sendError(401, "Unauthorized: " + ex.getMessage())
                        )
                        .accessDeniedHandler((request, response, ex) ->
                                response.sendError(403, "Access Denied: " + ex.getMessage())
                        )
                );

        // Add JWT Filter before UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
