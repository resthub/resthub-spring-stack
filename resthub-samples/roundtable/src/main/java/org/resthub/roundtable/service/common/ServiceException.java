package org.resthub.roundtable.service.common;

/**
 * Service exception.
 * @author Nicolas Carlier (mailto:pouicbox@yahoo.fr)
 */
public class ServiceException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public ServiceException() {
        super();
    }

    /**
     * @param message message
     * @param cause cause
     */
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message message
     */
    public ServiceException(String message) {
        super(message);
    }
}
