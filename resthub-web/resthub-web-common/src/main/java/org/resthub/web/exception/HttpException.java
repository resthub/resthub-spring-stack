package org.resthub.web.exception;

/**
 * Base exception class for HTTP error requests
 */
public class HttpException extends RuntimeException {
    
    private int statusCode;
    
    public HttpException() {
        super();
    }
    
    public HttpException(int statusCode) {
        super();
        this.statusCode = statusCode;
    }

    public HttpException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public HttpException(final String message) {
        super(message);
    }

    public HttpException(final Throwable cause) {
        super(cause);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }   
    
}
