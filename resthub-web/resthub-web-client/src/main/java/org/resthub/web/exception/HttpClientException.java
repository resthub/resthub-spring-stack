package org.resthub.web.exception;

@SuppressWarnings("serial")
public class HttpClientException extends RuntimeException {
	
	public HttpClientException(){
		super();
	}
	public HttpClientException( final String message, final Throwable cause ){
		super( message + " -> " + cause.getMessage(), cause );
	}
	public HttpClientException( final String message ){
		super( message );
	}
	public HttpClientException( final Throwable cause ){
		super( cause );
	}

}
