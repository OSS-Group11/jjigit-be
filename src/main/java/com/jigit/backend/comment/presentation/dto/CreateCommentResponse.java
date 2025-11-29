package com.jigit.backend.comment.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Schema(description = "Comment creation response")
public class CreateCommentResponse {

    @Schema(description = "Comment ID", example = "1")
    private Long commentId;

    @Schema(description = "Author ID", example = "123")
    private Long authorId;

    @Schema(description = "Comment content", example = "Great poll!")
    private String content;

    @Schema(description = "Creation timestamp", example = "2025-01-26T10:30:00")
    private LocalDateTime createdAt;
}
