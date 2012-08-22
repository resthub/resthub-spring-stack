package org.resthub.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception mapped to Conflict HTTP status code (409)
 */
@SuppressWarnings("serial")
@ResponseStatus(value = HttpStatus.CONFLICT)
public class ConflictException extends RuntimeException {

    public ConflictException() {
        super();
    }

    public ConflictException(final String message, final Throwable cause) {
        super(message + " -> " + cause.getMessage(), cause);
    }

    public ConflictException(final String message) {
        super(message);
    }

    public ConflictException(final Throwable cause) {
        super(cause);
    }
}
