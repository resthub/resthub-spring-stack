package org.resthub.web.exception;

import org.resthub.web.Http;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception mapped to forbidden Request HTTP status code (403)
 */
@SuppressWarnings("serial")
@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class ForbiddenException extends HttpClientErrorException { 

    public ForbiddenException() {
        super();
        this.setStatusCode(Http.FORBIDDEN);
    }

    public ForbiddenException(final String message, final Throwable cause) {
        super(message, cause);
        this.setStatusCode(Http.FORBIDDEN);
    }

    public ForbiddenException(final String message) {
        super(message);
        this.setStatusCode(Http.FORBIDDEN);
    }

    public ForbiddenException(final Throwable cause) {
        super(cause);
        this.setStatusCode(Http.FORBIDDEN);
    }

}
