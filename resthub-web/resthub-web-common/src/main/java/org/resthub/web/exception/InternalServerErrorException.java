package org.resthub.web.exception;

import org.resthub.web.Http;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception mapped to Internal Server Error HTTP status code (500)
 */
@SuppressWarnings("serial")
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerErrorException extends HttpServerErrorException {

    public InternalServerErrorException() {
        super();
        this.setStatusCode(Http.INTERNAL_SERVER_ERROR);
    }

    public InternalServerErrorException(final String message, final Throwable cause) {
        super(message, cause);
        this.setStatusCode(Http.INTERNAL_SERVER_ERROR);
    }

    public InternalServerErrorException(final String message) {
        super(message);
        this.setStatusCode(Http.INTERNAL_SERVER_ERROR);
    }

    public InternalServerErrorException(final Throwable cause) {
        super(cause);
        this.setStatusCode(Http.INTERNAL_SERVER_ERROR);
    }
}
