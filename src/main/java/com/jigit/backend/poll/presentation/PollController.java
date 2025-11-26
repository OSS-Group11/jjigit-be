package com.jigit.backend.poll.presentation;

import com.jigit.backend.global.util.JwtUtil;
import com.jigit.backend.poll.application.PollService;
import com.jigit.backend.poll.presentation.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for poll operations
 * Implements PollControllerDocs for Swagger documentation
 */
@RestController
@RequestMapping("/api/polls")
@RequiredArgsConstructor
public class PollController implements PollControllerDocs {

    private final PollService pollService;
    private final JwtUtil jwtUtil;

    /**
     * Create a new poll with options
     * @param request poll creation request
     * @param authorizationHeader JWT token for authentication
     * @return poll creation response
     */
    @Override
    @PostMapping
    public ResponseEntity<CreatePollResponse> createPoll(
            @Valid @RequestBody CreatePollRequest request,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        String token = jwtUtil.extractTokenFromHeader(authorizationHeader);
        Long userId = jwtUtil.getUserIdFromToken(token);

        CreatePollResponse response = pollService.createPoll(request, userId);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieve a single poll by ID
     * @param pollId poll ID to retrieve
     * @return poll details response
     */
    @Override
    @GetMapping("/{pollId}")
    public ResponseEntity<GetPollResponse> getPollById(@PathVariable Long pollId) {
        GetPollResponse response = pollService.getPollById(pollId);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieve all public polls with pagination
     * @param pageable pagination information
     * @return paginated list of public polls
     */
    @Override
    @GetMapping
    public ResponseEntity<PollListResponse> getPublicPolls(
            @PageableDefault(size = 10) Pageable pageable
    ) {
        PollListResponse response = pollService.getPublicPolls(pageable);
        return ResponseEntity.ok(response);
    }
}
