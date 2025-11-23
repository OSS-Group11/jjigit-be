package com.jigit.backend.user.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Request DTO for user login
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User login request")
public class LoginRequest {

    @NotBlank(message = "Username is required")
    @Schema(description = "Username for login", example = "johndoe")
    private String username;

    @NotBlank(message = "Password is required")
    @Schema(description = "User password", example = "password123")
    private String password;
}
