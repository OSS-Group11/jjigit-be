package com.jigit.backend.poll.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Option entity.
 * Provides database access methods for poll options including atomic vote count updates.
 */
@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {

    /**
     * Find all options for a specific poll
     * @param poll the poll entity
     * @return List of options belonging to the poll
     */
    List<Option> findByPollOrderByOptionOrder(Poll poll);

    /**
     * Atomically increment vote count for an option.
     * Uses database-level atomic operation to prevent race conditions when multiple users vote concurrently.
     * CRITICAL: This query ensures thread-safe vote counting.
     *
     * @param optionId ID of the option to increment vote count for
     */
    @Modifying
    @Query("UPDATE Option o SET o.voteCount = o.voteCount + 1 WHERE o.optionId = :optionId")
    void incrementVoteCount(@Param("optionId") Long optionId);

    /**
     * Find all options for a poll, ordered by option order.
     *
     * @param pollId ID of the poll
     * @return List of options ordered by optionOrder field
     */
    List<Option> findByPoll_PollIdOrderByOptionOrderAsc(Long pollId);
}
