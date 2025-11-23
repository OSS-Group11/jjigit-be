package com.jigit.backend.vote.exception;

import com.jigit.backend.global.exception.ExceptionCode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum VoteException implements ExceptionCode {

    VOTE_NOT_FOUND(HttpStatus.NOT_FOUND, "Vote Not Found", "The requested vote does not exist."),
    DUPLICATE_VOTE(HttpStatus.CONFLICT, "Duplicate Vote", "You have already voted on this poll."),
    INVALID_OPTION(HttpStatus.BAD_REQUEST, "Invalid Option", "The selected option does not belong to this poll."),
    POLL_NOT_ACCESSIBLE(HttpStatus.FORBIDDEN, "Poll Not Accessible", "This poll is not accessible.");

    private final HttpStatus httpStatus;
    private final String title;
    private final String detail;

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDetail() {
        return detail;
    }
}
