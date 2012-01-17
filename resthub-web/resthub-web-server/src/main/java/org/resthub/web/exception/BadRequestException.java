package org.resthub.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus( value = HttpStatus.BAD_REQUEST )
public class BadRequestException extends RuntimeException {
	
	public BadRequestException(){
		super();
	}
	public BadRequestException( final String message, final Throwable cause ){
		super( message + " -> " + cause.getMessage() , cause );
	}
	public BadRequestException( final String message ){
		super( message );
	}
	public BadRequestException( final Throwable cause ){
		super( cause );
	}

}
