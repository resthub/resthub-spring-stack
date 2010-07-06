package org.resthub.oauth2.provider.front;

import javax.inject.Named;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.resthub.oauth2.provider.exception.ProtocolException;
import org.resthub.oauth2.provider.front.model.ObtainTokenErrorResponse;

/**
 * Jersey Handler for ProtocolException raised in Controller or Service objects.
 */
@Provider
@Named("protocolExceptionHandler")
public class ProtocolExceptionHandler implements ExceptionMapper<ProtocolException> {

	/**
	 * Invoked by jersey when a ProtocolException is raised.
	 * 
	 * @param exception Exception catched.
	 * @return The corresponding HTTP Response.
	 */
	@Override
	public Response toResponse(ProtocolException exception) {
		// HTTP code is always 400.
		ResponseBuilder builder = Response.status(Status.BAD_REQUEST);
		// Response body.
		ObtainTokenErrorResponse response = new ObtainTokenErrorResponse(exception);
		builder.entity(response);
		// Cache control
		CacheControl noStore = new CacheControl();
		noStore.setNoStore(true);
		builder.cacheControl(noStore);
		// Response format
		builder.type(MediaType.APPLICATION_JSON);
		return builder.build();
	} // toResponse().
}
