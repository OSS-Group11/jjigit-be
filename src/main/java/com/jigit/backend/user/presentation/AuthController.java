package com.jigit.backend.user.presentation;

import com.jigit.backend.user.application.AuthService;
import com.jigit.backend.user.presentation.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for authentication operations
 * Implements AuthControllerDocs for Swagger documentation
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController implements AuthControllerDocs {

    private final AuthService authService;

    /**
     * Register a new user
     * @param request signup request containing username and password
     * @return signup response with created user ID
     */
    @Override
    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@Valid @RequestBody SignupRequest request) {
        SignupResponse response = authService.signup(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Authenticate user and generate JWT token
     * @param request login request containing username and password
     * @return login response with JWT token and user ID
     */
    @Override
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Validate JWT token and extract user information
     * @param authorizationHeader the Authorization header containing Bearer token
     * @return validation response with validity status and user ID
     */
    @Override
    @GetMapping("/validate")
    public ResponseEntity<ValidateResponse> validateToken(
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        ValidateResponse response = authService.validateToken(authorizationHeader);
        return ResponseEntity.ok(response);
    }
}
