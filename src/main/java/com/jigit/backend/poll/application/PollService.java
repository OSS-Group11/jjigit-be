package com.jigit.backend.poll.application;

import com.jigit.backend.global.exception.ApplicationException;
import com.jigit.backend.poll.domain.Option;
import com.jigit.backend.poll.domain.OptionRepository;
import com.jigit.backend.poll.domain.Poll;
import com.jigit.backend.poll.domain.PollRepository;
import com.jigit.backend.poll.exception.PollException;
import com.jigit.backend.poll.presentation.dto.*;
import com.jigit.backend.user.domain.User;
import com.jigit.backend.user.domain.UserRepository;
import com.jigit.backend.user.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for poll operations
 * Handles poll creation, retrieval, and listing
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PollService {

    private final PollRepository pollRepository;
    private final OptionRepository optionRepository;
    private final UserRepository userRepository;

    /**
     * Create a new poll with options
     * @param request poll creation request containing title, visibility, and options
     * @param userId creator user ID
     * @return poll creation response
     * @throws ApplicationException if user not found or validation fails
     */
    @Transactional
    public CreatePollResponse createPoll(CreatePollRequest request, Long userId) {
        // Validate user exists
        User creator = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(UserException.USER_NOT_FOUND));

        // Create and save poll
        Poll poll = Poll.builder()
                .creator(creator)
                .title(request.getTitle())
                .isPublic(request.getIsPublic())
                .build();

        Poll savedPoll = pollRepository.save(poll);

        // Create and save options
        List<Option> options = request.getOptions().stream()
                .map(optionReq -> Option.builder()
                        .poll(savedPoll)
                        .optionText(optionReq.getOptionText())
                        .optionOrder(optionReq.getOptionOrder())
                        .build())
                .collect(Collectors.toList());

        List<Option> savedOptions = optionRepository.saveAll(options);

        // Map to response
        List<OptionResponse> optionResponses = savedOptions.stream()
                .map(option -> new OptionResponse(
                        option.getOptionId(),
                        option.getOptionText(),
                        option.getOptionOrder(),
                        option.getVoteCount()
                ))
                .collect(Collectors.toList());

        return new CreatePollResponse(
                savedPoll.getPollId(),
                savedPoll.getTitle(),
                savedPoll.getIsPublic(),
                optionResponses,
                creator.getUserId(),
                savedPoll.getCreatedAt()
        );
    }

    /**
     * Retrieve a single poll by ID
     * @param pollId poll ID to retrieve
     * @return poll details response
     * @throws ApplicationException if poll not found
     */
    public GetPollResponse getPollById(Long pollId) {
        // Find poll
        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new ApplicationException(PollException.POLL_NOT_FOUND));

        // Find options
        List<Option> options = optionRepository.findByPollOrderByOptionOrder(poll);

        // Map to response
        List<OptionResponse> optionResponses = options.stream()
                .map(option -> new OptionResponse(
                        option.getOptionId(),
                        option.getOptionText(),
                        option.getOptionOrder(),
                        option.getVoteCount()
                ))
                .collect(Collectors.toList());

        // Calculate total votes (sum of all option voteCounts)
        Integer totalVotes = options.stream()
                .mapToInt(Option::getVoteCount)
                .sum();

        return new GetPollResponse(
                poll.getPollId(),
                poll.getTitle(),
                poll.getIsPublic(),
                optionResponses,
                poll.getCreator().getUserId(),
                poll.getCreatedAt(),
                totalVotes // ← 실제 전체 투표수 반영
        );
    }


    /**
     * Retrieve all public polls with pagination
     * @param pageable pagination information
     * @return paginated list of public polls
     */
    public PollListResponse getPublicPolls(Pageable pageable) {
        Page<Poll> pollPage = pollRepository.findByIsPublicTrue(pageable);

        List<GetPollResponse> pollResponses = pollPage.getContent().stream()
                .map(poll -> {
                    List<Option> options = optionRepository.findByPollOrderByOptionOrder(poll);

                    // Build option responses with actual vote counts
                    List<OptionResponse> optionResponses = options.stream()
                            .map(option -> new OptionResponse(
                                    option.getOptionId(),
                                    option.getOptionText(),
                                    option.getOptionOrder(),
                                    option.getVoteCount() // 실제 투표수 반영
                            ))
                            .collect(Collectors.toList());

                    // Calculate total votes
                    Integer totalVotes = options.stream()
                            .mapToInt(Option::getVoteCount)
                            .sum();

                    return new GetPollResponse(
                            poll.getPollId(),
                            poll.getTitle(),
                            poll.getIsPublic(),
                            optionResponses,
                            poll.getCreator().getUserId(),
                            poll.getCreatedAt(),
                            totalVotes // 전체 투표수 반영
                    );
                })
                .collect(Collectors.toList());

        return new PollListResponse(
                pollResponses,
                pollPage.getNumber(),
                pollPage.getTotalPages(),
                pollPage.getTotalElements(),
                pollPage.getSize()
        );
    }

}
