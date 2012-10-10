package org.resthub.web.exception;

/**
 * Base exception class for HTTP server error requests (status code = 5xx)
 */
public class HttpServerErrorException extends HttpException {
    
    public HttpServerErrorException() {
        super();
    }
    
    public HttpServerErrorException(int statusCode) {
        super(statusCode);
    }

    public HttpServerErrorException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public HttpServerErrorException(final String message) {
        super(message);
    }

    public HttpServerErrorException(final Throwable cause) {
        super(cause);
    }
    
}
