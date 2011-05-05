package org.resthub.tapestry5.security;

/**
 * Exception to managed basic errors on the authentication proccess
 * 
 * @author karesti
 * 
 */
public class AuthenticationException extends Exception {

    private static final long serialVersionUID = 7740628210842909239L;

    public AuthenticationException() {
        super();

    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);

    }

    public AuthenticationException(String message) {
        super(message);

    }

    public AuthenticationException(Throwable cause) {
        super(cause);

    }

}
