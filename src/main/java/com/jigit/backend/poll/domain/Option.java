package com.jigit.backend.poll.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "options")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_id")
    private Long optionId;

    @Schema(hidden = true)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poll_id", nullable = false)
    private Poll poll;

    @Column(name = "option_text", nullable = false, length = 255)
    private String optionText;

    @Column(name = "option_order", nullable = false)
    private Integer optionOrder;

    @Builder
    public Option(Poll poll, String optionText, Integer optionOrder) {
        this.poll = poll;
        this.optionText = optionText;
        this.optionOrder = optionOrder;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }
}
