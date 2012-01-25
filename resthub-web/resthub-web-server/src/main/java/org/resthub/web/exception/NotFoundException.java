package org.resthub.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus( value = HttpStatus.NOT_FOUND )
public class NotFoundException extends RuntimeException {

	public NotFoundException(){
		super();
	}
	public NotFoundException( final String message, final Throwable cause ){
		super( message + " -> " + cause.getMessage(), cause );
	}
	public NotFoundException( final String message ){
		super( message );
	}
	public NotFoundException( final Throwable cause ){
		super( cause );
	}
	
}
