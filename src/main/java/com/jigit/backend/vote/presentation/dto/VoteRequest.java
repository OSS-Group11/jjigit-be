package com.jigit.backend.vote.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Vote submission request")
public class VoteRequest {

    @NotNull(message = "Option ID is required")
    @Schema(description = "ID of the selected option", example = "1")
    private Long optionId;
}