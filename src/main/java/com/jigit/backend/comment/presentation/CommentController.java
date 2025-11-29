package com.jigit.backend.comment.presentation;

import com.jigit.backend.comment.application.CommentService;
import com.jigit.backend.comment.presentation.dto.CommentListResponse;
import com.jigit.backend.comment.presentation.dto.CreateCommentRequest;
import com.jigit.backend.comment.presentation.dto.CreateCommentResponse;
import com.jigit.backend.global.auth.CurrentUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for comment-related endpoints.
 * Handles comment creation and retrieval with voter information.
 */
@RestController
@RequestMapping("/api/polls")
@RequiredArgsConstructor
public class CommentController implements CommentControllerDocs {

    private final CommentService commentService;

    /**
     * Create a comment on a poll.
     * Requires JWT authentication.
     *
     * @param pollId ID of the poll
     * @param request Comment creation request containing content
     * @param userId Current user's ID (extracted from JWT token via @CurrentUser)
     * @return ResponseEntity with created comment details
     */
    @Override
    @PostMapping("/{pollId}/comments")
    public ResponseEntity<CreateCommentResponse> createComment(
            @PathVariable Long pollId,
            @Valid @RequestBody CreateCommentRequest request,
            @CurrentUser Long userId
    ) {
        CreateCommentResponse response = commentService.createComment(userId, pollId, request.getContent());
        return ResponseEntity.ok(response);
    }

    /**
     * Get all comments for a poll with author's voting information.
     * Public endpoint - no authentication required.
     *
     * @param pollId ID of the poll
     * @param sortBy Sort order (optional: "newest" or "oldest", defaults to "newest")
     * @return ResponseEntity with list of comments including voter information
     */
    @Override
    @GetMapping("/{pollId}/comments")
    public ResponseEntity<CommentListResponse> getComments(
            @PathVariable Long pollId,
            @RequestParam(required = false, defaultValue = "newest") String sortBy
    ) {
        CommentListResponse response = commentService.getComments(pollId, sortBy);
        return ResponseEntity.ok(response);
    }
}
