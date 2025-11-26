package com.jigit.backend.vote.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Vote entity.
 * Provides database access methods for vote records.
 */
@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    /**
     * Check if user has already voted on a poll.
     * Leverages the UNIQUE constraint on (voter_id, poll_id) for data integrity.
     *
     * @param userId ID of the user
     * @param pollId ID of the poll
     * @return true if user has voted, false otherwise
     */
    boolean existsByVoter_UserIdAndPoll_PollId(Long userId, Long pollId);

    /**
     * Find a user's vote on a specific poll.
     *
     * @param userId ID of the user
     * @param pollId ID of the poll
     * @return Optional containing the vote if found, empty otherwise
     */
    Optional<Vote> findByVoter_UserIdAndPoll_PollId(Long userId, Long pollId);
}