package com.jigit.backend.vote.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Schema(description = "Poll results response")
public class PollResultsResponse {

    @Schema(description = "Poll ID", example = "1")
    private Long pollId;

    @Schema(description = "Poll title", example = "Favorite programming language?")
    private String title;

    @Schema(description = "Total number of votes", example = "42")
    private Integer totalVotes;

    @Schema(description = "Results for each option")
    private List<OptionResultDto> options;
}