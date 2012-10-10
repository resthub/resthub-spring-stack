package org.resthub.web.exception;

/**
 * Base exception class for HTTP client error requests (status code = 4xx)
 */
public class HttpClientErrorException extends HttpException {
    
    public HttpClientErrorException() {
        super();
    }
    
    public HttpClientErrorException(int statusCode) {
        super(statusCode);
    }

    public HttpClientErrorException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public HttpClientErrorException(final String message) {
        super(message);
    }

    public HttpClientErrorException(final Throwable cause) {
        super(cause);
    }
    
}
