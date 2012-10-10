package org.resthub.web.exception;

import org.resthub.web.Http;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception mapped to unauthorized Request HTTP status code (401)
 */
@SuppressWarnings("serial")
@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends HttpClientErrorException { 

    public UnauthorizedException() {
        super();
        this.setStatusCode(Http.UNAUTHORIZED);
    }

    public UnauthorizedException(final String message, final Throwable cause) {
        super(message, cause);
        this.setStatusCode(Http.UNAUTHORIZED);
    }

    public UnauthorizedException(final String message) {
        super(message);
        this.setStatusCode(Http.UNAUTHORIZED);
    }

    public UnauthorizedException(final Throwable cause) {
        super(cause);
        this.setStatusCode(Http.UNAUTHORIZED);
    }

}
