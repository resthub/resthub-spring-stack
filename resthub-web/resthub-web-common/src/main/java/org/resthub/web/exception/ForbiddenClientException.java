package org.resthub.web.exception;

import org.resthub.web.Http;

/**
 * Exception mapped to forbidden Request HTTP status code (403)
 */
@SuppressWarnings("serial")
public class ForbiddenClientException extends ClientException { 

    public ForbiddenClientException() {
        super();
        this.setStatusCode(Http.FORBIDDEN);
    }

    public ForbiddenClientException(final String message, final Throwable cause) {
        super(message, cause);
        this.setStatusCode(Http.FORBIDDEN);
    }

    public ForbiddenClientException(final String message) {
        super(message);
        this.setStatusCode(Http.FORBIDDEN);
    }

    public ForbiddenClientException(final Throwable cause) {
        super(cause);
        this.setStatusCode(Http.FORBIDDEN);
    }

}
