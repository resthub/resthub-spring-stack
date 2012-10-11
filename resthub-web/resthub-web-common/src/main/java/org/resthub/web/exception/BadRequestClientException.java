package org.resthub.web.exception;

import org.resthub.web.Http;

/**
 * Exception mapped to Bad Request HTTP status code (400)
 */
@SuppressWarnings("serial")
public class BadRequestClientException extends ClientException { 

    public BadRequestClientException() {
        super();
        this.setStatusCode(Http.BAD_REQUEST);
    }

    public BadRequestClientException(final String message, final Throwable cause) {
        super(message, cause);
        this.setStatusCode(Http.BAD_REQUEST);
    }

    public BadRequestClientException(final String message) {
        super(message);
        this.setStatusCode(Http.BAD_REQUEST);
    }

    public BadRequestClientException(final Throwable cause) {
        super(cause);
        this.setStatusCode(Http.BAD_REQUEST);
    }

}
