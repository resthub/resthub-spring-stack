package org.resthub.oauth2.provider.web;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.resthub.oauth2.common.model.Token;

/**
 * REST Controller for authorisation requests.
 */
@Path("/authorize/")
public interface AuthorizationController {

	/**
	 * Token edpoint to obtain access token with "Resource Owner Basic Credentials" as described
	 * in the OAuth 2 specification (Section 4.1.2).
	 * 
	 * The only gant_type supported is "password".
	 * The only way to send client credentials is the use of query parameters.
	 * 
	 * @param clientId Client identifier. <b>Not used now, must be null</b>.
	 * @param clientSecret Client secret. <b>Not used now, must be null</b>.
	 * @param grant Access grant type. <b>For now, only "password" supported</b>.
	 * @param scopes Space separated list of object that will be accessed. <b>For now, must be empty.</b>.
	 * @param userName End-user name. 
	 * @param password End-user password.
	 * @return An TokenResponse (HTTP 200) if the request is successful, a ObtainTokenErrorResponse (HTTP 400) otherwise.
	 * 
	 * @throws ProtocolException INVALID_REQUEST: if the request is missing a required parameter, includes an unknown 
	 * parameter or parameter value, repeats a parameter, includes multiple credentials, utilizes more than one 
	 * mechanism for authenticating the client, or is otherwise malformed.
	 * @throws ProtocolException UNSUPPORTED_GRANT_TYPE: if the grant-type parameter is not 'password'.
	 * @throws ProtocolException INVALID_CLIENT_CREDENTIALS: if the client Identifier and password are not empty.
	 * @throws ProtocolException INVALID_SCOPE: if the scope parameter is not well formated, or if it's not empty.
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
	
	/**
	 * Retrieves all information relative to an access token.
	 * 
	 * @param accessToken The access token (query parameter access_token").
	 * @param password The service shared secret (header value of "Authorization" header).
	 * @return A Token object (HTTP 200) containing all informations, or null if no access token found (HTTP 204)
	 * 
	 * @throws IllegalArgumentException If the accessToken parameter is missing.
	 */
	@GET
	@Path("tokenDetails")
	@Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
	Token obtainTokenInformation(@QueryParam("access_token")String accessToken,
			@HeaderParam(HttpHeaders.AUTHORIZATION)String password);
	
} // interface AuthorizationController
