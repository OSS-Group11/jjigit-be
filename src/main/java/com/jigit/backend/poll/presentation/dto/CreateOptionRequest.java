package com.jigit.backend.poll.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Request DTO for creating a poll option
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Poll option creation request")
public class CreateOptionRequest {

    @NotBlank(message = "Option text is required")
    @Schema(description = "Option text content", example = "Pineapple on pizza is acceptable")
    private String optionText;

    @NotNull(message = "Option order is required")
    @Schema(description = "Display order of the option", example = "1")
    private Integer optionOrder;
}
