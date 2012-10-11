package org.resthub.web.exception;

import org.resthub.web.Http;

/**
 * Exception mapped to not acceptable Error HTTP status code (406)
 */
@SuppressWarnings("serial")
public class NotAcceptableClientException extends ClientException {

    public NotAcceptableClientException() {
        super();
        this.setStatusCode(Http.NOT_ACCEPTABLE);
    }

    public NotAcceptableClientException(final String message, final Throwable cause) {
        super(message, cause);
        this.setStatusCode(Http.NOT_ACCEPTABLE);
    }

    public NotAcceptableClientException(final String message) {
        super(message);
        this.setStatusCode(Http.NOT_ACCEPTABLE);
    }

    public NotAcceptableClientException(final Throwable cause) {
        super(cause);
        this.setStatusCode(Http.NOT_ACCEPTABLE);
    }
}
