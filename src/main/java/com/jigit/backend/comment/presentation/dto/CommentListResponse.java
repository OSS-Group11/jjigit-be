package com.jigit.backend.comment.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Schema(description = "Comment list response")
public class CommentListResponse {

    @Schema(description = "List of comments")
    private List<CommentResponse> comments;
}
