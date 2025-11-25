package com.jigit.backend.vote.domain;

import com.jigit.backend.poll.domain.Option;
import com.jigit.backend.poll.domain.Poll;
import com.jigit.backend.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "votes",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_voter_poll",
            columnNames = {"voter_id", "poll_id"}
        )
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_id")
    private Long voteId;

    @Schema(hidden = true)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poll_id", nullable = false)
    private Poll poll;

    @Schema(hidden = true)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id", nullable = false)
    private Option option;

    @Schema(hidden = true)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voter_id", nullable = false)
    private User voter;

    @Column(name = "voted_at", nullable = false, updatable = false)
    private LocalDateTime votedAt;

    @Builder
    public Vote(Poll poll, Option option, User voter) {
        this.poll = poll;
        this.option = option;
        this.voter = voter;
        this.votedAt = LocalDateTime.now();
    }
}
