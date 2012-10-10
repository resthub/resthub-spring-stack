package org.resthub.web.exception;

import org.resthub.web.Http;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception mapped to Conflict HTTP status code (409)
 */
@SuppressWarnings("serial")
@ResponseStatus(value = HttpStatus.CONFLICT)
public class ConflictException extends HttpClientErrorException {

    public ConflictException() {
        super();
        this.setStatusCode(Http.CONFLICT);
    }

    public ConflictException(final String message, final Throwable cause) {
        super(message, cause);
        this.setStatusCode(Http.CONFLICT);
    }

    public ConflictException(final String message) {
        super(message);
        this.setStatusCode(Http.CONFLICT);
    }

    public ConflictException(final Throwable cause) {
        super(cause);
        this.setStatusCode(Http.CONFLICT);
    }
}
