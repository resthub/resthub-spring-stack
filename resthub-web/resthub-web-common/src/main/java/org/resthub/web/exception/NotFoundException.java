package org.resthub.web.exception;

import org.resthub.web.Http;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception mapped to not found Error HTTP status code (404)
 */
@SuppressWarnings("serial")
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundException extends HttpClientErrorException {

    public NotFoundException() {
        super();
        this.setStatusCode(Http.NOT_FOUND);
    }

    public NotFoundException(final String message, final Throwable cause) {
        super(message, cause);
        this.setStatusCode(Http.NOT_FOUND);
    }

    public NotFoundException(final String message) {
        super(message);
        this.setStatusCode(Http.NOT_FOUND);
    }

    public NotFoundException(final Throwable cause) {
        super(cause);
        this.setStatusCode(Http.NOT_FOUND);
    }
}
