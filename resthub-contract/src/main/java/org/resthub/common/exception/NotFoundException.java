package org.resthub.common.exception;

/**
 * Exception thrown when not result was found (for example findById with null return value)
 */
public class NotFoundException extends RuntimeException {
    
    public NotFoundException() {
        super();
    }

    public NotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public NotFoundException(final String message) {
        super(message);
    }

    public NotFoundException(final Throwable cause) {
        super(cause);
    }
    
}
