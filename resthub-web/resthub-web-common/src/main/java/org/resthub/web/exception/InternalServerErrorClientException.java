package org.resthub.web.exception;

import org.resthub.web.Http;

/**
 * Exception mapped to Internal Server Error HTTP status code (500)
 */
@SuppressWarnings("serial")
public class InternalServerErrorClientException extends ClientException {

    public InternalServerErrorClientException() {
        super();
        this.setStatusCode(Http.INTERNAL_SERVER_ERROR);
    }

    public InternalServerErrorClientException(final String message, final Throwable cause) {
        super(message, cause);
        this.setStatusCode(Http.INTERNAL_SERVER_ERROR);
    }

    public InternalServerErrorClientException(final String message) {
        super(message);
        this.setStatusCode(Http.INTERNAL_SERVER_ERROR);
    }

    public InternalServerErrorClientException(final Throwable cause) {
        super(cause);
        this.setStatusCode(Http.INTERNAL_SERVER_ERROR);
    }
}
