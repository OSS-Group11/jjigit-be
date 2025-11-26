package com.jigit.backend.poll.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Option entity
 * Provides database access methods for option-related operations
 */
@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {

    /**
     * Find all options for a specific poll
     * @param poll the poll entity
     * @return List of options belonging to the poll
     */
    List<Option> findByPollOrderByOptionOrder(Poll poll);
}
