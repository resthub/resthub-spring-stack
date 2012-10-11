package org.resthub.web.exception;

import org.resthub.web.Http;

/**
 * Exception mapped to Conflict HTTP status code (409)
 */
@SuppressWarnings("serial")
public class ConflictClientException extends ClientException {

    public ConflictClientException() {
        super();
        this.setStatusCode(Http.CONFLICT);
    }

    public ConflictClientException(final String message, final Throwable cause) {
        super(message, cause);
        this.setStatusCode(Http.CONFLICT);
    }

    public ConflictClientException(final String message) {
        super(message);
        this.setStatusCode(Http.CONFLICT);
    }

    public ConflictClientException(final Throwable cause) {
        super(cause);
        this.setStatusCode(Http.CONFLICT);
    }
}
