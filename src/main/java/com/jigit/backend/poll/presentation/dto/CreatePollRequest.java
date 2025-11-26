package com.jigit.backend.poll.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Request DTO for creating a poll
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Poll creation request")
public class CreatePollRequest {

    @NotBlank(message = "Poll title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    @Schema(description = "Poll title", example = "Pineapple on pizza: acceptable or not?")
    private String title;

    @NotNull(message = "Poll visibility is required")
    @Schema(description = "Whether the poll is public or private", example = "true")
    private Boolean isPublic;

    @Valid
    @NotNull(message = "Options are required")
    @Size(min = 2, message = "At least 2 options are required")
    @Schema(description = "List of poll options (minimum 2)")
    private List<CreateOptionRequest> options;
}
