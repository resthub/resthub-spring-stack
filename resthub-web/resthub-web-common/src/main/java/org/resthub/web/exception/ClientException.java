package org.resthub.web.exception;

/**
 * Base exception class for HTTP error requests
 */
public class ClientException extends RuntimeException {
    
    private int statusCode;
    
    public ClientException() {
        super();
    }
    
    public ClientException(final int statusCode) {
        super();
        this.statusCode = statusCode;
    }
    
    public ClientException(final int statusCode, final String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public ClientException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ClientException(final String message) {
        super(message);
    }

    public ClientException(final Throwable cause) {
        super(cause);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }   
    
}
