package com.jigit.backend.vote.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "Individual option result")
public class OptionResultDto {

    @Schema(description = "Option ID", example = "1")
    private Long optionId;

    @Schema(description = "Option text", example = "Java")
    private String optionText;

    @Schema(description = "Number of votes", example = "15")
    private Integer voteCount;

    @Schema(description = "Percentage of total votes", example = "35.71")
    private Double percentage;
}