package com.jigit.backend.comment.presentation;

import com.jigit.backend.comment.presentation.dto.CommentListResponse;
import com.jigit.backend.comment.presentation.dto.CreateCommentRequest;
import com.jigit.backend.comment.presentation.dto.CreateCommentResponse;
import com.jigit.backend.global.auth.CurrentUser;
import com.jigit.backend.global.exception.ErrorResponse;
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
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Swagger documentation interface for Comment API endpoints.
 */
@Tag(name = "Comment", description = "댓글 작성 및 조회 API")
public interface CommentControllerDocs {

    @Operation(
            summary = "댓글 작성",
            description = "투표에 댓글을 작성합니다. 인증된 사용자만 가능합니다.\n\n" +
                    "**요청 예시:**\n" +
                    "```json\n" +
                    "{\n" +
                    "  \"content\": \"이 투표 정말 재미있네요! 저는 1번 선택했어요.\"\n" +
                    "}\n" +
                    "```"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "댓글 작성 성공",
                    content = @Content(schema = @Schema(implementation = CreateCommentResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 - 댓글 내용이 비어있음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
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
    ResponseEntity<CreateCommentResponse> createComment(
            @PathVariable Long pollId,
            @RequestBody CreateCommentRequest request,
            @Parameter(hidden = true) @CurrentUser Long userId
    );

    @Operation(
            summary = "댓글 목록 조회",
            description = """
                    투표의 모든 댓글을 조회합니다. 각 댓글에는 작성자가 투표한 옵션 정보도 포함됩니다. 인증 불필요.

                    **Query Parameters:**
                    - `sortBy`: 정렬 순서 (기본값: "newest")
                      - `newest`: 최신순 (최근 작성된 댓글이 먼저)
                      - `oldest`: 오래된순 (오래 전 작성된 댓글이 먼저)

                    **사용 예시:**
                    - `/api/polls/1/comments` - 최신순 정렬 (기본값)
                    - `/api/polls/1/comments?sortBy=newest` - 최신순 정렬
                    - `/api/polls/1/comments?sortBy=oldest` - 오래된순 정렬
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "댓글 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = CommentListResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "투표를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    ResponseEntity<CommentListResponse> getComments(
            @Parameter(description = "조회할 투표 ID", example = "1")
            @PathVariable Long pollId,
            @Parameter(
                    description = "정렬 순서 (newest: 최신순, oldest: 오래된순)",
                    example = "newest"
            )
            @RequestParam(required = false, defaultValue = "newest") String sortBy
    );
}
