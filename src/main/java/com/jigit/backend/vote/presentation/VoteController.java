package com.jigit.backend.vote.presentation;

import com.jigit.backend.global.auth.CurrentUser;
import com.jigit.backend.vote.application.VoteService;
import com.jigit.backend.vote.presentation.dto.PollResultsResponse;
import com.jigit.backend.vote.presentation.dto.VoteRequest;
import com.jigit.backend.vote.presentation.dto.VoteResponse;
import com.jigit.backend.vote.presentation.dto.VoteStatusResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for vote-related endpoints.
 * Handles vote submission, status checks, and result queries.
 */
@RestController
@RequestMapping("/api/polls")
@RequiredArgsConstructor
public class VoteController implements VoteControllerDocs {

    private final VoteService voteService;

    /**
     * Submit a vote for a poll option.
     * Requires JWT authentication.
     *
     * @param pollId ID of the poll
     * @param request Vote request containing selected option ID
     * @param userId Current user's ID (extracted from JWT token via @CurrentUser)
     * @return ResponseEntity with vote confirmation message
     */
    @Override
    @PostMapping("/{pollId}/vote")
    public ResponseEntity<VoteResponse> submitVote(
            @PathVariable Long pollId,
            @Valid @RequestBody VoteRequest request,
            @CurrentUser Long userId
    ) {
        VoteResponse response = voteService.submitVote(userId, pollId, request.getOptionId());
        return ResponseEntity.ok(response);
    }

    /**
     * Check if current user has voted on a poll.
     * Requires JWT authentication.
     *
     * @param pollId ID of the poll
     * @param userId Current user's ID (extracted from JWT token via @CurrentUser)
     * @return ResponseEntity with vote status and selected option ID
     */
    @Override
    @GetMapping("/{pollId}/voted")
    public ResponseEntity<VoteStatusResponse> checkVoteStatus(
            @PathVariable Long pollId,
            @CurrentUser Long userId
    ) {
        VoteStatusResponse response = voteService.checkVoteStatus(userId, pollId);
        return ResponseEntity.ok(response);
    }

    /**
     * Get aggregated poll results.
     * Public endpoint - no authentication required.
     *
     * @param pollId ID of the poll
     * @return ResponseEntity with poll results including vote counts and percentages
     */
    @Override
    @GetMapping("/{pollId}/results")
    public ResponseEntity<PollResultsResponse> getPollResults(
            @PathVariable Long pollId
    ) {
        PollResultsResponse response = voteService.getPollResults(pollId);
        return ResponseEntity.ok(response);
    }
}