package com.jigit.backend.user.application;

import com.jigit.backend.global.exception.ApplicationException;
import com.jigit.backend.global.util.JwtUtil;
import com.jigit.backend.user.domain.User;
import com.jigit.backend.user.domain.UserRepository;
import com.jigit.backend.user.exception.UserException;
import com.jigit.backend.user.presentation.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for authentication operations
 * Handles user signup, login, and token validation
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Register a new user
     * @param request signup request containing username and password
     * @return signup response with created user ID
     * @throws ApplicationException if username already exists
     */
    @Transactional
    public SignupResponse signup(SignupRequest request) {
        // Check for duplicate username
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ApplicationException(UserException.DUPLICATE_USERNAME);
        }

        // Hash password using bcrypt
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        // Create and save user
        User user = User.builder()
                .username(request.getUsername())
                .passwordHash(hashedPassword)
                .build();

        User savedUser = userRepository.save(user);

        return new SignupResponse(savedUser.getUserId());
    }

    /**
     * Authenticate user and generate JWT token
     * @param request login request containing username and password
     * @return login response with JWT token and user ID
     * @throws ApplicationException if credentials are invalid
     */
    public LoginResponse login(LoginRequest request) {
        // Find user by username
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ApplicationException(UserException.INVALID_CREDENTIALS));

        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new ApplicationException(UserException.INVALID_CREDENTIALS);
        }

        // Generate JWT token
        String token = jwtUtil.generateToken(user.getUserId());

        return new LoginResponse(token, user.getUserId());
    }

    /**
     * Validate JWT token and extract user information
     * @param authorizationHeader the Authorization header containing Bearer token
     * @return validation response with validity status and user ID
     */
    public ValidateResponse validateToken(String authorizationHeader) {
        // Extract token from header
        String token = jwtUtil.extractTokenFromHeader(authorizationHeader);

        if (token == null) {
            return new ValidateResponse(false, null);
        }

        // Validate token
        if (!jwtUtil.validateToken(token)) {
            return new ValidateResponse(false, null);
        }

        try {
            // Extract user ID from token
            Long userId = jwtUtil.getUserIdFromToken(token);
            return new ValidateResponse(true, userId);
        } catch (ApplicationException e) {
            return new ValidateResponse(false, null);
        }
    }
}
