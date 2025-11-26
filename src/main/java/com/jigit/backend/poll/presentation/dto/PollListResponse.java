package com.jigit.backend.poll.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * Response DTO for paginated poll list
 */
@Getter
@AllArgsConstructor
@Schema(description = "Paginated poll list response")
public class PollListResponse {

    @Schema(description = "List of polls")
    private List<GetPollResponse> polls;

    @Schema(description = "Current page number (0-indexed)", example = "0")
    private Integer currentPage;

    @Schema(description = "Total number of pages", example = "5")
    private Integer totalPages;

    @Schema(description = "Total number of polls", example = "50")
    private Long totalElements;

    @Schema(description = "Number of items per page", example = "10")
    private Integer pageSize;
}
