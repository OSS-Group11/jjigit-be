package com.jigit.backend.poll.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Poll entity
 * Provides database access methods for poll-related operations
 */
@Repository
public interface PollRepository extends JpaRepository<Poll, Long> {

    /**
     * Find all public polls with pagination
     * @param pageable pagination information
     * @return Page of public polls
     */
    Page<Poll> findByIsPublicTrue(Pageable pageable);
}
