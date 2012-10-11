package org.resthub.web.exception;

import org.resthub.web.Http;

/**
 * Exception mapped to not implemented Error HTTP status code (501)
 */
@SuppressWarnings("serial")
public class NotImplementedClientException extends ClientException {

    public NotImplementedClientException() {
        super();
        this.setStatusCode(Http.NOT_IMPLEMENTED);
    }

    public NotImplementedClientException(final String message, final Throwable cause) {
        super(message, cause);
        this.setStatusCode(Http.NOT_IMPLEMENTED);
    }

    public NotImplementedClientException(final String message) {
        super(message);
        this.setStatusCode(Http.NOT_IMPLEMENTED);
    }

    public NotImplementedClientException(final Throwable cause) {
        super(cause);
        this.setStatusCode(Http.NOT_IMPLEMENTED);
    }
}
