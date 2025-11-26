package com.jigit.backend.poll.presentation;

import com.jigit.backend.global.auth.CurrentUser;
import com.jigit.backend.poll.application.PollService;
import com.jigit.backend.poll.presentation.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    /**
     * Create a new poll with options
     * @param request poll creation request
     * @param userId current user's ID (extracted from JWT token via @CurrentUser)
     * @return poll creation response
     */
    @Override
    @PostMapping
    public ResponseEntity<CreatePollResponse> createPoll(
            @Valid @RequestBody CreatePollRequest request,
            @CurrentUser Long userId
    ) {
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
     * @param page page number (0-indexed)
     * @param size page size
     * @param sort sort field and direction (e.g., "createdAt,desc")
     * @return paginated list of public polls
     */
    @Override
    @GetMapping
    public ResponseEntity<PollListResponse> getPublicPolls(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort
    ) {
        // Parse sort parameter (e.g., "createdAt,desc" -> Sort.by(Sort.Order.desc("createdAt")))
        String[] sortParams = sort.split(",");
        Sort.Direction direction = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("asc")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;
        Sort sortBy = Sort.by(direction, sortParams[0]);

        Pageable pageable = PageRequest.of(page, size, sortBy);
        PollListResponse response = pollService.getPublicPolls(pageable);
        return ResponseEntity.ok(response);
    }
}
