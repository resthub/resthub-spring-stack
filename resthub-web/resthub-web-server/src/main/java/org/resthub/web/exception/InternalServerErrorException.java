package org.resthub.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception mapped to Internal Server Error HTTP status code (500)
 */
@SuppressWarnings("serial")
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerErrorException extends RuntimeException {

    public InternalServerErrorException() {
        super();
    }

    public InternalServerErrorException(final String message, final Throwable cause) {
        super(message + " -> " + cause.getMessage(), cause);
    }

    public InternalServerErrorException(final String message) {
        super(message);
    }

    public InternalServerErrorException(final Throwable cause) {
        super(cause);
    }
}
