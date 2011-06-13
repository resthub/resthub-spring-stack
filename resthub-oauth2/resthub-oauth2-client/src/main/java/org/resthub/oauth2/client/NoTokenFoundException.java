package org.resthub.oauth2.client;

/**
 * Exception used when the TokenRepository can not enrich a request with a valid
 * token.
 */
public class NoTokenFoundException extends RuntimeException {

    private static final long serialVersionUID = -1858322056181195952L;

    /**
     * Constructor.
     * 
     * @param message
     *            Reason why a NoTokenFoundException have been raised.
     */
    public NoTokenFoundException(String message) {
        super(message);
    } // Constructor.

    /**
     * Constructor.
     * 
     * @param message
     *            Reason why a NoTokenFoundException have been raised.
     * @param cause
     *            Root cause.
     */
    public NoTokenFoundException(String message, Throwable cause) {
        super(message, cause);
    } // Constructor.

} // Class NoTokenFoundException
