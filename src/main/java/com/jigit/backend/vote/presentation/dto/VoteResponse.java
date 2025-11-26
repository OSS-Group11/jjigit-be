package com.jigit.backend.vote.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "Vote submission response")
public class VoteResponse {

    @Schema(description = "Success message", example = "Vote submitted successfully")
    private String message;
}