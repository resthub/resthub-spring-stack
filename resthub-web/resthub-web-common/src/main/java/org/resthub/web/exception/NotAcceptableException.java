package org.resthub.web.exception;

import org.resthub.web.Http;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception mapped to not acceptable Error HTTP status code (406)
 */
@SuppressWarnings("serial")
@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class NotAcceptableException extends HttpClientErrorException {

    public NotAcceptableException() {
        super();
        this.setStatusCode(Http.NOT_ACCEPTABLE);
    }

    public NotAcceptableException(final String message, final Throwable cause) {
        super(message, cause);
        this.setStatusCode(Http.NOT_ACCEPTABLE);
    }

    public NotAcceptableException(final String message) {
        super(message);
        this.setStatusCode(Http.NOT_ACCEPTABLE);
    }

    public NotAcceptableException(final Throwable cause) {
        super(cause);
        this.setStatusCode(Http.NOT_ACCEPTABLE);
    }
}
