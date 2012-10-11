package org.resthub.web.exception;

import org.resthub.web.Http;

/**
 * Exception mapped to unauthorized Request HTTP status code (401)
 */
@SuppressWarnings("serial")
public class UnauthorizedClientException extends ClientException { 

    public UnauthorizedClientException() {
        super();
        this.setStatusCode(Http.UNAUTHORIZED);
    }

    public UnauthorizedClientException(final String message, final Throwable cause) {
        super(message, cause);
        this.setStatusCode(Http.UNAUTHORIZED);
    }

    public UnauthorizedClientException(final String message) {
        super(message);
        this.setStatusCode(Http.UNAUTHORIZED);
    }

    public UnauthorizedClientException(final Throwable cause) {
        super(cause);
        this.setStatusCode(Http.UNAUTHORIZED);
    }

}
