package com.jigit.backend.comment.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Comment entity.
 * Provides database access methods for comment records.
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * Find all comments for a specific poll, ordered by creation time (newest first).
     *
     * @param pollId ID of the poll
     * @return List of comments ordered by createdAt descending
     */
    List<Comment> findByPoll_PollIdOrderByCreatedAtDesc(Long pollId);

    /**
     * Find all comments for a specific poll, ordered by creation time (oldest first).
     *
     * @param pollId ID of the poll
     * @return List of comments ordered by createdAt ascending
     */
    List<Comment> findByPoll_PollIdOrderByCreatedAtAsc(Long pollId);
}
