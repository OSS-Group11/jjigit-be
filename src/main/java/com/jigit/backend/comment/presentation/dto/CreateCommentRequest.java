package com.jigit.backend.comment.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Comment creation request")
public class CreateCommentRequest {

    @NotBlank(message = "Comment content is required")
    @Schema(description = "Comment content", example = "Great poll! I agree with option 1.")
    private String content;
}
