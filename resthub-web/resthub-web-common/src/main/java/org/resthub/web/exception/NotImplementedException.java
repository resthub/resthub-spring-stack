package org.resthub.web.exception;

import org.resthub.web.Http;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception mapped to not implemented Error HTTP status code (501)
 */
@SuppressWarnings("serial")
@ResponseStatus(value = HttpStatus.NOT_IMPLEMENTED)
public class NotImplementedException extends HttpServerErrorException {

    public NotImplementedException() {
        super();
        this.setStatusCode(Http.NOT_IMPLEMENTED);
    }

    public NotImplementedException(final String message, final Throwable cause) {
        super(message, cause);
        this.setStatusCode(Http.NOT_IMPLEMENTED);
    }

    public NotImplementedException(final String message) {
        super(message);
        this.setStatusCode(Http.NOT_IMPLEMENTED);
    }

    public NotImplementedException(final Throwable cause) {
        super(cause);
        this.setStatusCode(Http.NOT_IMPLEMENTED);
    }
}
