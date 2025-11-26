package com.jigit.backend.comment.application;

import com.jigit.backend.comment.domain.Comment;
import com.jigit.backend.comment.domain.CommentRepository;
import com.jigit.backend.comment.exception.CommentException;
import com.jigit.backend.comment.presentation.dto.CommentListResponse;
import com.jigit.backend.comment.presentation.dto.CommentResponse;
import com.jigit.backend.comment.presentation.dto.CreateCommentResponse;
import com.jigit.backend.global.exception.ApplicationException;
import com.jigit.backend.poll.domain.Poll;
import com.jigit.backend.poll.domain.PollRepository;
import com.jigit.backend.poll.exception.PollException;
import com.jigit.backend.user.domain.User;
import com.jigit.backend.user.domain.UserRepository;
import com.jigit.backend.user.exception.UserException;
import com.jigit.backend.vote.domain.Vote;
import com.jigit.backend.vote.domain.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for comment-related business logic.
 * Handles comment creation and retrieval with voter information.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final PollRepository pollRepository;
    private final UserRepository userRepository;
    private final VoteRepository voteRepository;

    /**
     * Create a new comment on a poll.
     *
     * @param userId ID of the commenting user
     * @param pollId ID of the poll
     * @param content Comment content
     * @return CreateCommentResponse with comment details
     * @throws ApplicationException if validation fails
     */
    @Transactional
    public CreateCommentResponse createComment(Long userId, Long pollId, String content) {
        // 1. Validate content
        if (content == null || content.trim().isEmpty()) {
            throw new ApplicationException(CommentException.INVALID_COMMENT_CONTENT);
        }

        // 2. Validate poll exists
        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new ApplicationException(PollException.POLL_NOT_FOUND));

        // 3. Validate user exists
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(UserException.USER_NOT_FOUND));

        // 4. Create and save comment
        Comment comment = Comment.builder()
                .poll(poll)
                .author(author)
                .content(content.trim())
                .build();

        Comment savedComment = commentRepository.save(comment);

        return new CreateCommentResponse(
                savedComment.getCommentId(),
                savedComment.getAuthor().getUserId(),
                savedComment.getContent(),
                savedComment.getCreatedAt()
        );
    }

    /**
     * Get all comments for a poll with author's voting information.
     * Includes votedOptionId for each comment to show which option the commenter voted for.
     *
     * @param pollId ID of the poll
     * @param sortBy Sort order ("newest" or "oldest", defaults to "newest")
     * @return CommentListResponse with comments and voter information
     * @throws ApplicationException if poll not found
     */
    public CommentListResponse getComments(Long pollId, String sortBy) {
        // 1. Validate poll exists
        if (!pollRepository.existsById(pollId)) {
            throw new ApplicationException(PollException.POLL_NOT_FOUND);
        }

        // 2. Fetch comments based on sort order
        List<Comment> comments;
        if ("oldest".equalsIgnoreCase(sortBy)) {
            comments = commentRepository.findByPoll_PollIdOrderByCreatedAtAsc(pollId);
        } else {
            // Default to newest
            comments = commentRepository.findByPoll_PollIdOrderByCreatedAtDesc(pollId);
        }

        // 3. Map comments to response DTOs with voter information
        List<CommentResponse> commentResponses = comments.stream()
                .map(comment -> {
                    Long authorId = comment.getAuthor().getUserId();
                    String authorUsername = comment.getAuthor().getUsername();

                    // Check if comment author voted on this poll
                    Optional<Vote> vote = voteRepository.findByVoter_UserIdAndPoll_PollId(authorId, pollId);
                    Long votedOptionId = vote.map(v -> v.getOption().getOptionId()).orElse(null);

                    return new CommentResponse(
                            comment.getCommentId(),
                            authorId,
                            authorUsername,
                            comment.getContent(),
                            votedOptionId,
                            comment.getCreatedAt()
                    );
                })
                .collect(Collectors.toList());

        return new CommentListResponse(commentResponses);
    }
}
