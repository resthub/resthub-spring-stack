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
@Path("/authorize")
public interface AuthorizationController {

	/**
	 * Access code obtention end-point, also known as "end-user authentication end-point".<br/><br/>
	 * 
	 * Defined in the OAuth 2 specification v11 at section (4.1): client redirect user-agent to this end-point in 
	 * order to obtain the end-user agreement on accessing a protected resource.<br/><br/>
	 * 
	 * The aggreement is materliazed by an access code returned to the client, with which the client gets directly from
	 * the authorization server an access token for the desired resource.<br/><br/>
	 * 
	 * If an error occured, the user-agent is redirected to the redirection URI with additionnal parameters:
	 * error, error_description and state.<br/>
	 * If an error occured due to a redirection problem, an error response is returned.
	 * 
	 * @param responseType The requested response: an access token (value <code>token</code>), an authorization code
	 * (value <code>code</code>), or both (value <code>code_and_token</code>).<b>Only the code option is implemented</b> 
	 * @param clientId The client identifier.
	 * @param redirectUri An absolute URI to which the authorization server will redirect the user-agent to when the 
	 * end-user authorization step is completed.
	 * @param scopes The scope of the access request expressed as a list of space-delimited strings. <b>For now,
	 * must be empty.</b>.
	 * @param state An opaque and optional value used by the client to maintain state between the request and callback. 
	 * The authorization server includes this value when redirecting the user-agent back to the client.  
	 * 
	 * @return A page displaying the access demand and waiting for end-user aggreement and credentials (HTTP 200),
	 * or an ObtainTokenErrorResponse (HTTP 400) otherwise.
	 * 
	 * @throw ProtocolException INVALID_REQUEST: if the request is missing a required parameter, includes an unknown 
	 * parameter or parameter value, repeats a parameter, or is otherwise malformed.
	 * @throw ProtocolException REDIRECT_URI_MISMATCH: The redirection URI provided does not match a pre-registered 
	 * value.  (Not used for now).
	 */
	@GET
	Response obtainAccessCode(
			@QueryParam("response_type") String responseType,
			@QueryParam("client_id") String clientId,
			@QueryParam("redirect_uri") String redirectUri,
			@QueryParam("scope") String scopes,
			@QueryParam("state") String state
	);
	
	/**
	 * Token edpoint to obtain access token with "Resource Owner Password Credentials" as described
	 * in the OAuth 2 specification (Section 5.1.2).<br/><br/>
	 * 
	 * The only gant_type supported is "password".<br/>
	 * The only way to send client credentials is the use of query parameters.<br/><br/>
	 * 
	 * @param clientId Client identifier. <b>Not used now, must be null</b>.
	 * @param clientSecret Client secret. <b>Not used now, must be null</b>.
	 * @param grant Access grant type. <b>For now, only "password" supported</b>.
	 * @param scopes Space separated list of object that will be accessed. <b>For now, must be empty.</b>.
	 * @param userName End-user name. 
	 * @param password End-user password.
	 * @return An TokenResponse (HTTP 200) if the request is successful, a ObtainTokenErrorResponse (HTTP 400) otherwise.
	 * 
	 * @throw ProtocolException INVALID_REQUEST: if the request is missing a required parameter, includes an unknown 
	 * parameter or parameter value, repeats a parameter, includes multiple credentials, utilizes more than one 
	 * mechanism for authenticating the client, or is otherwise malformed.
	 * @throw ProtocolException UNSUPPORTED_GRANT_TYPE: if the grant_type parameter is not 'password'.
	 * @throw ProtocolException INVALID_CLIENT_CREDENTIALS: if the client Identifier and password are not empty.
	 * @throw ProtocolException INVALID_SCOPE: if the scope parameter is not well formated, or if it's not empty.
	 */
	@POST
	@Path("/token")
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
	 * @throw IllegalArgumentException If the accessToken parameter is missing.
	 */
	@GET
	@Path("/tokenDetails")
	@Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
	Token obtainTokenInformation(@QueryParam("access_token")String accessToken,
			@HeaderParam(HttpHeaders.AUTHORIZATION)String password);

	/**
	 * Invoked by the authentication page to perform end-user authentication.<br/>
	 * Once the end-user has accepted, its user-agent is redirected to the redirect URI with its state and an 
	 * additinonal access code (parameter <code>code</code>).<br/><br/>
	 * 
	 * @param userName End-user name. 
	 * @param password End-user password.
	 * @param redirectUri An absolute URI to which the authorization server will redirect the user-agent to when the 
	 * end-user authorization step is completed.
	 * @param state An opaque and optional value used by the client to maintain state between the request and callback. 
	 * The authorization server includes this value when redirecting the user-agent back to the client.  
	 * @return A redirection to the redirect URI (HTTP 302) or an ObtainTokenError if the credential are wrong (HTTP 204)
	 * 
	 * @throw ProtocolException INVALID_REQUEST: if one of the parameter is missing or its value misformated.
	 * @throw ProtocolException INVALID_CLIENT_CREDENTIALS: if the client Identifier and password are not empty.
	 */
	@POST
	@Path("/authenticate")
	Response authenticateEndUser(
			@FormParam("username") String username,
			@FormParam("password") String password,
			@FormParam("redirect_uri") String redirectUri,
			@FormParam("state") String state);

} // interface AuthorizationController
