package org.resthub.oauth2.filter.front;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.resthub.oauth2.common.exception.ProtocolException;
import org.resthub.oauth2.common.exception.ProtocolException.Error;
import org.resthub.oauth2.common.front.model.TokenResponse;
import org.resthub.oauth2.common.model.Token;
import org.resthub.web.jackson.JacksonProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.representation.Form;

/**
 * Class used to mutualize redirection steps used during Web-Server profile.
 */
public class WebServerUtilities {

	// -----------------------------------------------------------------------------------------------------------------
	// Private attributes

	/**
	 * Class logger.
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Jersey REST WebService client.
	 */
	protected Client wsClient;

	// -----------------------------------------------------------------------------------------------------------------
	// Public properties

	/**
	 * Absolute url of the Authorization Server where incoming request will be redirected (the "end-user authorization
	 * end-point").
	 */
	protected String authorizationServer = "";

	/**
	 * Used to inject the authorization server url when working with spring.
	 * 
	 * @param authorizationServer the injected authorization server address.
	 */
	public void setAuthorizationServer(String authorizationServer) {
		this.authorizationServer = authorizationServer;
	} // setAuthorizationServer().

	/**
	 * Client Id used to communicate with the Authorization server.
	 */
	protected String clientId = null;

	/**
	 * Used to inject the client id when working with spring.
	 * 
	 * @param clientId The injected client id.
	 */
	public void setClientId(String clientId) {
		this.clientId = clientId;
	} // setClientId().

	/**
	 * Client Id used to communicate with the Authorization server.
	 */
	protected String clientSecret = null;

	/**
	 * Used to inject the client secret when working with spring.
	 * 
	 * @param clientSecret The injected client secret.
	 */
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	} // setClientSecret().

	/**
	 * Name of the query parameter that contains the access tokent when retrieving the token details.
	 */
	protected String accessTokenParam = "";

	/**
	 * Used to inject the name of the query parameter that contains the access tokent when retrieving the token details
	 * when working with spring.
	 * 
	 * @param accessTokenParam The injected query parameter name.
	 */
	public void setAccessTokenParam(String accessTokenParam) {
		this.accessTokenParam = accessTokenParam;
	} // setAccessTokenParam().
	
	/**
	 * Password used to get token information on the central authorization service.
	 */
	protected String authorizationPassword = "";

	/**
	 * Used to inject Password used to get token information on the central authorization service when working with 
	 * spring.
	 * 
	 * @param authorizationPassword The injected authorization password.
	 */
	public void setAuthorizationPassword(String authorizationPassword) {
		this.authorizationPassword = authorizationPassword;
	} // setAuthorizationPassword().

	// -----------------------------------------------------------------------------------------------------------------
	// Constructor;
	
	/**
	 * Default constructor. Initialize the web client.
	 */
	public WebServerUtilities() {
		logger.trace("[Constructor] REST WS client initialization");
		ClientConfig config = new DefaultClientConfig();
	    config.getSingletons().add(new JacksonProvider());
	    wsClient = Client.create(config);
	} // Constructor.
	
	// -----------------------------------------------------------------------------------------------------------------
	// Public methods

	/**
	 * Redirect the incoming request to the end-user authentication end-point at the Authorization Server.
	 * 
	 * @param targetedUrl The targeted URL, requested by the user.
	 * @param request The incoming request.
	 * @param response The redirected response.
	 * @param withState if true, the targetedUrl is stored as state.
	 * 
	 * @throws UnsupportedEncodingException If the requested URL cannot be URL-encoded.
	 * @throws IOException If response cannot be redirected.
	 */
	public void redirectToAuthenticationEndPoint(String targetedUrl, HttpServletRequest request, 
			HttpServletResponse response, Boolean withState) throws Exception {
		// No access code, lets redirect to the authentication server.
		// Build redirection url.
		StringBuilder redirection = new StringBuilder(authorizationServer)
				.append("?response_type=code&client_id=")
				.append(clientId)
				.append("&redirect_uri=")
				.append(URLEncoder.encode(request.getRequestURL().toString(), "UTF-8"));
		if (withState) {
			redirection.append("&state=").append(URLEncoder.encode(targetedUrl, "UTF-8"));
		}
		logger.trace("[doFilter] redirection to {}", redirection.toString());
		// Redirect incoming request.
		response.sendRedirect(redirection.toString());		
	} // redirectToAuthenticationEndPoint().
	
	/**
	 * Performs an HTTP request to the authorization server with code, retrieving the 
	 * corresponding token.<br/><br/>
	 * 
	 * @param code The access code send by the Authorization server.
	 * @param request The incoming request.
	 * @param response The outgoing response.
	 * 
	 * @return The corresponding Token.
	 * 
	 * @throws ProtocolException INVALID_GRANT - If the token cannot be retrieved.
	 */
	public TokenResponse obtainTokenFromCode(String code, HttpServletRequest request, HttpServletResponse response) {
		logger.debug("[obtainTokenFromCode] Get token from code {}", code);
		// Performs a request to get token.
		WebResource connector = wsClient.resource(authorizationServer);
		Form form = new Form();
		form.add("grant_type", "authorization_code");
		form.add("client_id", clientId);
		form.add("client_secret", clientSecret);
		form.add("code", code);
		form.add("redirect_uri", request.getRequestURL().toString());
		TokenResponse tokenResponse = null;
		try {
			logger.trace("[obtainTokenFromCode] Send request to {}...", authorizationServer);
			tokenResponse = connector.path("token").type(MediaType.APPLICATION_FORM_URLENCODED).
					post(TokenResponse.class, form);
			logger.trace("[obtainTokenFromCode] Token retrieved !");
		} catch (Exception exc) {
			String error = "Token cannot be retrieved: " + exc.getMessage();
			logger.warn("[obtainTokenFromCode] " + error, exc);
			throw new ProtocolException(Error.INVALID_GRANT, error, exc);
		}
		return tokenResponse;
	} // obtainTokenFromCode().

	/**
	 * Performs an HTTP request to the authorization server with access token, retrieving User details.<br/><br/>
	 * 
	 * @param accessToken The token access code.
	 * 
	 * @return The corresponding Token details.
	 * 
	 * @throws ProtocolException INVALID_TOKEN - If the token details cannot be retrieved.
	 */
	public Token obtainTokenDetails(String accessToken) {
		logger.debug("[obtainTokenDetails] Get token details from token {}", accessToken);
		Token token =null;
		WebResource connector = wsClient.resource(authorizationServer);
		try {
			token = connector.path("tokenDetails").queryParam(accessTokenParam, accessToken).
				header(HttpHeaders.AUTHORIZATION, authorizationPassword).get(Token.class);
		} catch (UniformInterfaceException exc) {
			// Do not trace exception for unknown tokens.
			if (exc.getResponse().getStatus() != Status.NO_CONTENT.getStatusCode()) {
				logger.warn("[obtainTokenDetails] Cannot retrieved information on token '" + token.accessToken + "'", exc);
			}
		} catch (Exception exc) {
			String error = "Cannot retrieve information on token '" + token.accessToken + "': " + exc.getMessage();
			logger.warn("[obtainTokenDetails] " + error, exc);
			throw new ProtocolException(Error.INVALID_TOKEN, error, exc);
		}
		return token;
	} // obtainTokenFromCode().

} // class WebServerUtilities
