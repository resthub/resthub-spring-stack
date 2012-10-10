package org.resthub.web.exception;

import org.resthub.web.Http;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception mapped to Bad Request HTTP status code (400)
 */
@SuppressWarnings("serial")
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestException extends HttpClientErrorException { 

    public BadRequestException() {
        super();
        this.setStatusCode(Http.BAD_REQUEST);
    }

    public BadRequestException(final String message, final Throwable cause) {
        super(message, cause);
        this.setStatusCode(Http.BAD_REQUEST);
    }

    public BadRequestException(final String message) {
        super(message);
        this.setStatusCode(Http.BAD_REQUEST);
    }

    public BadRequestException(final Throwable cause) {
        super(cause);
        this.setStatusCode(Http.BAD_REQUEST);
    }

}
