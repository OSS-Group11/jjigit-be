package com.jigit.backend.vote.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "Vote status check response")
public class VoteStatusResponse {

    @Schema(description = "Whether user has voted on this poll", example = "true")
    private Boolean hasVoted;

    @Schema(description = "ID of the option user voted for (null if not voted)", example = "1")
    private Long votedOptionId;
}