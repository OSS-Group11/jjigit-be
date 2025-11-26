package com.jigit.backend.poll.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Response DTO for retrieving poll details
 */
@Getter
@AllArgsConstructor
@Schema(description = "Poll details response")
public class GetPollResponse {

    @Schema(description = "Poll ID", example = "1")
    private Long pollId;

    @Schema(description = "Poll title", example = "Pineapple on pizza: acceptable or not?")
    private String title;

    @Schema(description = "Whether the poll is public", example = "true")
    private Boolean isPublic;

    @Schema(description = "List of poll options with vote counts")
    private List<OptionResponse> options;

    @Schema(description = "Creator user ID", example = "1")
    private Long creatorId;

    @Schema(description = "Poll creation timestamp", example = "2025-11-26T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Total number of votes", example = "150")
    private Long totalVotes;
}
