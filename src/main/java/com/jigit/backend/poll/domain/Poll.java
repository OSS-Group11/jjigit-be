package com.jigit.backend.poll.domain;

import com.jigit.backend.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "polls")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Poll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "poll_id")
    private Long pollId;

    @Schema(hidden = true)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "is_public", nullable = false)
    private Boolean isPublic;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public Poll(User creator, String title, Boolean isPublic) {
        this.creator = creator;
        this.title = title;
        this.isPublic = isPublic;
        this.createdAt = LocalDateTime.now();
    }
}
