package com.jigit.backend.vote.application;

import com.jigit.backend.global.exception.ApplicationException;
import com.jigit.backend.poll.domain.Option;
import com.jigit.backend.poll.domain.OptionRepository;
import com.jigit.backend.poll.domain.Poll;
import com.jigit.backend.poll.domain.PollRepository;
import com.jigit.backend.poll.exception.PollException;
import com.jigit.backend.user.domain.User;
import com.jigit.backend.user.domain.UserRepository;
import com.jigit.backend.user.exception.UserException;
import com.jigit.backend.vote.domain.Vote;
import com.jigit.backend.vote.domain.VoteRepository;
import com.jigit.backend.vote.exception.VoteException;
import com.jigit.backend.vote.presentation.dto.OptionResultDto;
import com.jigit.backend.vote.presentation.dto.PollResultsResponse;
import com.jigit.backend.vote.presentation.dto.VoteResponse;
import com.jigit.backend.vote.presentation.dto.VoteStatusResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for vote-related business logic.
 * Handles vote submission, status checks, and result aggregation.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VoteService {

    private final VoteRepository voteRepository;
    private final OptionRepository optionRepository;
    private final PollRepository pollRepository;
    private final UserRepository userRepository;

    /**
     * Submit a vote for a poll option.
     * Enforces 1-vote-per-poll constraint and validates option belongs to poll.
     * Uses atomic update query for vote count increment to ensure thread-safety.
     *
     * @param userId ID of the voting user
     * @param pollId ID of the poll
     * @param optionId ID of the selected option
     * @return VoteResponse with success message
     * @throws ApplicationException if validation fails or user already voted
     */
    @Transactional
    public VoteResponse submitVote(Long userId, Long pollId, Long optionId) {
        log.info("Vote submission attempt - UserId: {}, PollId: {}, OptionId: {}", userId, pollId, optionId);

        // 1. Check if user already voted on this poll
        if (voteRepository.existsByVoter_UserIdAndPoll_PollId(userId, pollId)) {
            log.warn("Vote failed - User already voted: UserId: {}, PollId: {}", userId, pollId);
            throw new ApplicationException(VoteException.DUPLICATE_VOTE);
        }

        // 2. Validate poll exists
        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new ApplicationException(PollException.POLL_NOT_FOUND));

        // 3. Validate option exists
        Option option = optionRepository.findById(optionId)
                .orElseThrow(() -> new ApplicationException(PollException.OPTION_NOT_FOUND));

        // 4. Validate option belongs to this poll
        if (!option.getPoll().getPollId().equals(pollId)) {
            log.warn("Vote failed - Option does not belong to poll: OptionId: {}, PollId: {}", optionId, pollId);
            throw new ApplicationException(VoteException.INVALID_OPTION);
        }

        // 5. Fetch user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(UserException.USER_NOT_FOUND));

        // 6. Create and save vote record
        Vote vote = Vote.builder()
                .poll(poll)
                .option(option)
                .voter(user)
                .build();
        voteRepository.save(vote);

        // 7. Atomically increment vote count (thread-safe)
        optionRepository.incrementVoteCount(optionId);

        log.info("Vote submitted successfully - UserId: {}, PollId: {}, OptionId: {}", userId, pollId, optionId);
        return new VoteResponse("Vote submitted successfully");
    }

    /**
     * Check if user has voted on a poll.
     *
     * @param userId ID of the user
     * @param pollId ID of the poll
     * @return VoteStatusResponse with vote status and selected option ID
     * @throws ApplicationException if poll not found
     */
    public VoteStatusResponse checkVoteStatus(Long userId, Long pollId) {
        // Verify poll exists
        if (!pollRepository.existsById(pollId)) {
            throw new ApplicationException(PollException.POLL_NOT_FOUND);
        }

        // Check if user has voted
        Optional<Vote> vote = voteRepository.findByVoter_UserIdAndPoll_PollId(userId, pollId);

        if (vote.isPresent()) {
            return new VoteStatusResponse(true, vote.get().getOption().getOptionId());
        } else {
            return new VoteStatusResponse(false, null);
        }
    }

    /**
     * Get aggregated poll results with vote counts and percentages.
     * Public endpoint - no authentication required.
     *
     * @param pollId ID of the poll
     * @return PollResultsResponse with vote counts and percentages for each option
     * @throws ApplicationException if poll not found
     */
    public PollResultsResponse getPollResults(Long pollId) {
        // 1. Fetch poll
        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new ApplicationException(PollException.POLL_NOT_FOUND));

        // 2. Fetch all options with vote counts, ordered by optionOrder
        List<Option> options = optionRepository.findByPoll_PollIdOrderByOptionOrderAsc(pollId);

        // 3. Calculate total votes across all options
        int totalVotes = options.stream()
                .mapToInt(Option::getVoteCount)
                .sum();

        // 4. Build result DTOs with percentages
        List<OptionResultDto> optionResults = options.stream()
                .map(option -> {
                    // Calculate percentage (handle division by zero)
                    double percentage = totalVotes > 0
                            ? (option.getVoteCount() * 100.0 / totalVotes)
                            : 0.0;
                    // Round to 2 decimal places
                    percentage = Math.round(percentage * 100.0) / 100.0;

                    return new OptionResultDto(
                            option.getOptionId(),
                            option.getOptionText(),
                            option.getVoteCount(),
                            percentage
                    );
                })
                .collect(Collectors.toList());

        return new PollResultsResponse(
                poll.getPollId(),
                poll.getTitle(),
                totalVotes,
                optionResults
        );
    }
}