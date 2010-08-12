package org.resthub.oauth2.filter.front;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.resthub.oauth2.common.exception.ProtocolException.Error;

/**
 * Jersey Exception handler to set the WWW-Authenticate header when receiving a FORBIDDEN exception 
 * (insufficient rights).<br/><br/>
 * 
 * Used to handler Jersey own's exception raised when the JSR-250 annotations are parsed and failed on security issues.
 */
@Provider
public class WebApplicationExceptionHandler implements ExceptionMapper<WebApplicationException> {

	// -----------------------------------------------------------------------------------------------------------------
	// Protected attributes

	/**
	 * Resource protected and served by this server.
	 */
	protected String resource = "";

	// -----------------------------------------------------------------------------------------------------------------
	// Constructeur

	/**
	 * Constructor
	 */
	public WebApplicationExceptionHandler() {
		// TODO Initialized the resource name
	} // Default 
	
	// -----------------------------------------------------------------------------------------------------------------
	// Public ExceptionMapper inherited methods

	/**
	 * Invoked by jersey when a WebApplicationException is raised.
	 * 
	 * @param exception Exception catched.
	 * @return The corresponding HTTP Response.
	 */
	@Override
	public Response toResponse(WebApplicationException exception) {
		// In case of a 403 response, triggered by the authorization filter.
		Response response = null;
		if (exception.getResponse().getStatus() == Status.FORBIDDEN.getStatusCode()) {
			// HTTP code
			ResponseBuilder builder = Response.status(Error.INSUFFICIENT_SCOPE.status());
			StringBuilder sb = new StringBuilder();
			// Header value.
			sb.append("Token realm=\"").
				append(resource).
				append("\", error=\"").
				append(Error.INSUFFICIENT_SCOPE.value()).
				append("\"");
			if (exception.getMessage() != null) {
				sb.append(", error_description=\"").
					append(exception.getMessage()).
					append("\"");
			}
			// Sets the response header.
			builder.header(HttpHeaders.WWW_AUTHENTICATE, sb.toString());
			response = builder.build();
		} else {
			response = exception.getResponse();
		}
		return response;
	} // toResponse().
	
} // class WebApplicationExceptionHandler
