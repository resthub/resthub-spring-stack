package org.resthub.web.exception;

import org.resthub.web.Http;

/**
 * Exception mapped to not found Error HTTP status code (404)
 */
@SuppressWarnings("serial")
public class NotFoundClientException extends ClientException {

    public NotFoundClientException() {
        super();
        this.setStatusCode(Http.NOT_FOUND);
    }

    public NotFoundClientException(final String message, final Throwable cause) {
        super(message, cause);
        this.setStatusCode(Http.NOT_FOUND);
    }

    public NotFoundClientException(final String message) {
        super(message);
        this.setStatusCode(Http.NOT_FOUND);
    }

    public NotFoundClientException(final Throwable cause) {
        super(cause);
        this.setStatusCode(Http.NOT_FOUND);
    }
}
