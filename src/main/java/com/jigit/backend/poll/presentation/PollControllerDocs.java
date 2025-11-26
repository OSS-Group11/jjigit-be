package com.jigit.backend.poll.presentation;

import com.jigit.backend.global.exception.ErrorResponse;
import com.jigit.backend.poll.presentation.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * API documentation interface for poll operations
 */
@Tag(name = "Poll", description = "투표 관리 API")
public interface PollControllerDocs {

    @Operation(
            summary = "투표 생성",
            description = "새로운 투표를 생성합니다. 최소 2개 이상의 선택지가 필요하며, 공개/비공개 설정이 가능합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "투표 생성 성공",
                    content = @Content(
                            schema = @Schema(implementation = CreatePollResponse.class),
                            examples = @ExampleObject(
                                    name = "투표 생성 성공",
                                    value = """
                                            {
                                              "pollId": 1,
                                              "title": "파인애플 피자: 찬성 vs 반대",
                                              "isPublic": true,
                                              "options": [
                                                {
                                                  "optionId": 1,
                                                  "optionText": "파인애플 피자 찬성",
                                                  "optionOrder": 1,
                                                  "voteCount": 0
                                                },
                                                {
                                                  "optionId": 2,
                                                  "optionText": "파인애플 피자 반대",
                                                  "optionOrder": 2,
                                                  "voteCount": 0
                                                }
                                              ],
                                              "creatorId": 1,
                                              "createdAt": "2025-11-26T10:30:00"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "선택지 부족",
                                            value = """
                                                    {
                                                      "title": "선택지 부족",
                                                      "status": 400,
                                                      "detail": "투표는 최소 2개 이상의 선택지가 필요합니다.",
                                                      "instance": "/api/polls"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "유효하지 않은 제목",
                                            value = """
                                                    {
                                                      "title": "유효하지 않은 제목",
                                                      "status": 400,
                                                      "detail": "투표 제목이 유효하지 않습니다.",
                                                      "instance": "/api/polls"
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "사용자 없음",
                                    value = """
                                            {
                                              "title": "사용자를 찾을 수 없음",
                                              "status": 404,
                                              "detail": "요청한 사용자가 존재하지 않습니다.",
                                              "instance": "/api/polls"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "서버 내부 오류",
                                    value = """
                                            {
                                              "title": "서버 내부 오류",
                                              "status": 500,
                                              "detail": "투표 생성 중 오류가 발생했습니다.",
                                              "instance": "/api/polls"
                                            }
                                            """
                            )
                    )
            )
    })
    @PostMapping
    ResponseEntity<CreatePollResponse> createPoll(
            @Valid @RequestBody CreatePollRequest request,
            @RequestHeader("Authorization") String authorizationHeader
    );

    @Operation(
            summary = "투표 조회",
            description = "투표 ID로 특정 투표의 상세 정보를 조회합니다. 선택지별 투표 수가 포함됩니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "투표 조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = GetPollResponse.class),
                            examples = @ExampleObject(
                                    name = "투표 조회 성공",
                                    value = """
                                            {
                                              "pollId": 1,
                                              "title": "파인애플 피자: 찬성 vs 반대",
                                              "isPublic": true,
                                              "options": [
                                                {
                                                  "optionId": 1,
                                                  "optionText": "파인애플 피자 찬성",
                                                  "optionOrder": 1,
                                                  "voteCount": 42
                                                },
                                                {
                                                  "optionId": 2,
                                                  "optionText": "파인애플 피자 반대",
                                                  "optionOrder": 2,
                                                  "voteCount": 58
                                                }
                                              ],
                                              "creatorId": 1,
                                              "createdAt": "2025-11-26T10:30:00",
                                              "totalVotes": 100
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "404", description = "투표를 찾을 수 없음",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "투표 없음",
                                    value = """
                                            {
                                              "title": "투표를 찾을 수 없음",
                                              "status": 404,
                                              "detail": "요청한 투표가 존재하지 않습니다.",
                                              "instance": "/api/polls/999"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "서버 내부 오류",
                                    value = """
                                            {
                                              "title": "서버 내부 오류",
                                              "status": 500,
                                              "detail": "투표 조회 중 오류가 발생했습니다.",
                                              "instance": "/api/polls/1"
                                            }
                                            """
                            )
                    )
            )
    })
    @GetMapping("/{pollId}")
    ResponseEntity<GetPollResponse> getPollById(
            @Parameter(description = "조회할 투표 ID", example = "1")
            @PathVariable Long pollId
    );

    @Operation(
            summary = "공개 투표 목록 조회",
            description = "공개로 설정된 투표 목록을 페이지네이션하여 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "투표 목록 조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = PollListResponse.class),
                            examples = @ExampleObject(
                                    name = "투표 목록 조회 성공",
                                    value = """
                                            {
                                              "polls": [
                                                {
                                                  "pollId": 1,
                                                  "title": "파인애플 피자: 찬성 vs 반대",
                                                  "isPublic": true,
                                                  "options": [
                                                    {
                                                      "optionId": 1,
                                                      "optionText": "파인애플 피자 찬성",
                                                      "optionOrder": 1,
                                                      "voteCount": 42
                                                    },
                                                    {
                                                      "optionId": 2,
                                                      "optionText": "파인애플 피자 반대",
                                                      "optionOrder": 2,
                                                      "voteCount": 58
                                                    }
                                                  ],
                                                  "creatorId": 1,
                                                  "createdAt": "2025-11-26T10:30:00",
                                                  "totalVotes": 100
                                                },
                                                {
                                                  "pollId": 2,
                                                  "title": "빨대 구멍은 한 개 vs 두 개",
                                                  "isPublic": true,
                                                  "options": [
                                                    {
                                                      "optionId": 3,
                                                      "optionText": "한 개",
                                                      "optionOrder": 1,
                                                      "voteCount": 30
                                                    },
                                                    {
                                                      "optionId": 4,
                                                      "optionText": "두 개",
                                                      "optionOrder": 2,
                                                      "voteCount": 70
                                                    }
                                                  ],
                                                  "creatorId": 2,
                                                  "createdAt": "2025-11-26T11:00:00",
                                                  "totalVotes": 100
                                                }
                                              ],
                                              "currentPage": 0,
                                              "totalPages": 5,
                                              "totalElements": 50,
                                              "pageSize": 10
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "서버 내부 오류",
                                    value = """
                                            {
                                              "title": "서버 내부 오류",
                                              "status": 500,
                                              "detail": "투표 목록 조회 중 오류가 발생했습니다.",
                                              "instance": "/api/polls"
                                            }
                                            """
                            )
                    )
            )
    })
    @GetMapping
    ResponseEntity<PollListResponse> getPublicPolls(
            @Parameter(description = "페이지네이션 정보 (page, size, sort)")
            Pageable pageable
    );
}
