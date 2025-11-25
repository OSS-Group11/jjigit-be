package com.jigit.backend.user.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Response DTO for token validation
 */
@Getter
@AllArgsConstructor
@Schema(description = "Token validation response")
public class ValidateResponse {

    @Schema(description = "Whether the token is valid", example = "true")
    private Boolean isValid;

    @Schema(description = "User ID from token (null if invalid)", example = "1")
    private Long userId;
}
