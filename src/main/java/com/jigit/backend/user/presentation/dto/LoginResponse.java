package com.jigit.backend.user.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Response DTO for user login
 */
@Getter
@AllArgsConstructor
@Schema(description = "User login response")
public class LoginResponse {

    @Schema(description = "JWT authentication token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    @Schema(description = "User ID", example = "1")
    private Long userId;
}
