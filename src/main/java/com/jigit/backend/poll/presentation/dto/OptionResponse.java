package com.jigit.backend.poll.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Response DTO for poll option
 */
@Getter
@AllArgsConstructor
@Schema(description = "Poll option response")
public class OptionResponse {

    @Schema(description = "Option ID", example = "1")
    private Long optionId;

    @Schema(description = "Option text content", example = "Pineapple on pizza is acceptable")
    private String optionText;

    @Schema(description = "Display order of the option", example = "1")
    private Integer optionOrder;

    @Schema(description = "Number of votes for this option", example = "42")
    private Integer voteCount;
}
