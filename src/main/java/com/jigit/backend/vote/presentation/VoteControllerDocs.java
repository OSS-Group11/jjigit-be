package com.jigit.backend.vote.presentation;

import com.jigit.backend.global.auth.CurrentUser;
import com.jigit.backend.global.exception.ErrorResponse;
import com.jigit.backend.vote.presentation.dto.PollResultsResponse;
import com.jigit.backend.vote.presentation.dto.VoteRequest;
import com.jigit.backend.vote.presentation.dto.VoteResponse;
import com.jigit.backend.vote.presentation.dto.VoteStatusResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Swagger documentation interface for Vote API endpoints.
 */
@Tag(name = "Vote", description = "투표 참여 및 결과 조회 API")
public interface VoteControllerDocs {

    @Operation(
            summary = "투표하기",
            description = "투표의 특정 옵션에 투표합니다. 한 사용자는 한 투표당 한 번만 투표할 수 있습니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "투표 성공",
                    content = @Content(schema = @Schema(implementation = VoteResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 필요 - JWT 토큰이 유효하지 않거나 누락됨",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "투표 또는 옵션을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "이미 이 투표에 참여함",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    ResponseEntity<VoteResponse> submitVote(
            @Parameter(description = "투표 ID", example = "1")
            @PathVariable Long pollId,
            @RequestBody VoteRequest request,
            @Parameter(hidden = true) @CurrentUser Long userId
    );

    @Operation(
            summary = "투표 여부 확인",
            description = "현재 사용자가 이 투표에 참여했는지 확인합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "투표 상태 조회 성공",
                    content = @Content(schema = @Schema(implementation = VoteStatusResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 필요 - JWT 토큰이 유효하지 않거나 누락됨",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "투표를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    ResponseEntity<VoteStatusResponse> checkVoteStatus(
            @Parameter(description = "투표 ID", example = "1")
            @PathVariable Long pollId,
            @Parameter(hidden = true) @CurrentUser Long userId
    );

    @Operation(
            summary = "투표 결과 조회",
            description = "투표의 집계된 결과를 득표수와 백분율로 조회합니다. 인증 불필요 (공개 엔드포인트)."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "투표 결과 조회 성공",
                    content = @Content(schema = @Schema(implementation = PollResultsResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "투표를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    ResponseEntity<PollResultsResponse> getPollResults(
            @Parameter(description = "투표 ID", example = "1")
            @PathVariable Long pollId
    );
}