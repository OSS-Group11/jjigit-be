package com.jigit.backend.user.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Response DTO for user signup
 */
@Getter
@AllArgsConstructor
@Schema(description = "User signup response")
public class SignupResponse {

    @Schema(description = "Created user ID", example = "1")
    private Long userId;
}
