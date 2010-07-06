package org.resthub.oauth2.provider.front;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Controller for authorisation requests.
 */
@Path("/")
public interface AuthorizationController {

	/**
	 * Token edpoint to obtain access token with "Resource Owner Basic Credentials" as described
	 * in the OAuth 2 specification (Section 4.1.2).
	 * 
	 * The only gant_type supported is "basic-credentials".
	 * The only way to send client credentials is the use of query parameters.
	 * 
	 * @param clientId Client identifier. <b>Not used now, must be null</b>.
	 * @param clientSecret Client secret. <b>Not used now, must be null</b>.
	 * @param grant Access grant type. <b>For now, only "basic-credentials" supported</b>.
	 * @param scopes Space separated list of object that will be accessed. <b>For now, must be empty.</b>.
	 * @param userName End-user name. 
	 * @param password End-user password.
	 * 
	 * @throws ProtocolException INVALID_REQUEST: if the request is missing a required parameter, includes an unknown 
	 * parameter or parameter value, repeats a parameter, includes multiple credentials, utilizes more than one 
	 * mechanism for authenticating the client, or is otherwise malformed.
	 */
	@POST
	@Path("token")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	Response obtainAccessTokenBasicCredentials(
			@FormParam("client_id") String clientId, 
			@FormParam("client_secret") String clientSecret,
			@FormParam("grant_type") String grant,
			@FormParam("scope") String scopes,
			@FormParam("username") String userName,
			@FormParam("password") String password);
	
} // interface AuthorizationController
