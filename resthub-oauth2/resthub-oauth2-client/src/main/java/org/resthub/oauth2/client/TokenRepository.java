package org.resthub.oauth2.client;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.resthub.oauth2.common.front.model.TokenResponse;
import org.resthub.oauth2.utils.JacksonProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.representation.Form;

/**
 * Token repository is a light OAuth2 client which 
 * <ul><li>retrieve access token (associated to a specific resource and scope)</li>
 * <li>keep these tokens in memory</li>
 * <li>enrich an Http query with the right token.</li></ul>
 * 
 * You can use in many ways this utility class:
 * <ol><li>Just with enrich(). When enrich() will be invoked, existing token will be used, or if no token are available, 
 * a token will be asked.</li>
 * <li>With obtain(), add() and enrich(). Gets your token with obtain(), keeps it with add(), and it will be used when 
 * enrich() will be invoked.</li>
 * <li>With add(), and consult(). Sets your token manually with addToken(), and retrieves them furtherly with consult().
 * Token repository is just an in-memory storage space.</li></ol>
 * 
 * But in all ways, you'll need to indicate your client credential (NOT end-user credentials!), and locations of 
 * authentication services.
 */
public class TokenRepository {

	// -----------------------------------------------------------------------------------------------------------------
	// Protected attributes

	/**
	 * Class logger.
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Stores in a thread-safe way tokens associated to protected resources.
	 */
	protected ConcurrentHashMap<String, List<TokenResponse>> tokens = new ConcurrentHashMap<String, List<TokenResponse>>();
	
	/**
	 * Web client used to request authentication end points.
	 */
	protected Client httpClient;
	
	// -----------------------------------------------------------------------------------------------------------------
	// Public properties

	/**
	 * List of HTTP end-points of known authentication services.
	 */
	protected List<String> authenticationEndPoints = new ArrayList<String>();
	
	/**
	 * Used to inject the list of authentication end-points (urls).
	 * 
	 * @param value List of known authentication services.
	 */
	public void setAuthenticationServices(List<String> value) {
		if (value != null) {
			authenticationEndPoints.addAll(value);
		}
	} // setAuthenticationServices().
	
	/**
	 * Client id for this repository.
	 */
	protected String clientId = "";

	/**
	 * Used to inject the client id, used to obtain access tokens.
	 * 
	 * @param value The client id.
	 */
	public void setClientId(String value) {
		clientId = value;
	} // setClientId().

	/**
	 * Client secredt for this repository. 
	 */
	protected String clientSecret = "";
	
	/**
	 * Used to inject the client secret, used to obtain access tokens.
	 * 
	 * @param value The client secret.
	 */
	public void setClientSecret(String value) {
		clientSecret = value;
	} // setClientSecret().

	// -----------------------------------------------------------------------------------------------------------------
	// Constructor

	/**
	 * Default constructor.
	 */
	public TokenRepository() {
		ClientConfig config = new DefaultClientConfig();
        config.getSingletons().add(new JacksonProvider());
		httpClient = Client.create(config);
	} // Constructor.
	
	// -----------------------------------------------------------------------------------------------------------------
	// Public methods

	/**
	 * Ask each authentication services for an access token corresponding to the given resource.
	 * @param resource The resource from whom a token will be obtained.
	 * @param scope The desired scope.
	 * @return The obtained token.
	 * 
	 * @throws NoTokenFoundException If no token can be retrieved for this resource and scope.
	 */
	public TokenResponse obtain(String resource, String scope) throws NoTokenFoundException {
		logger.trace("[obtain] Try to get token for resource '" + resource + "' and scope '" + scope + "'");
		// Form sended to authentication servers.
		Form form = new Form();
		form.add("grant_type", "password");
		form.add("client_id", null);
		form.add("client_secret", null);
		// TODO utiliser client_id et client_secret.
		form.add("username", clientId);
		form.add("password", clientSecret);
		
		TokenResponse result = null;
		// Try each authentication servers.
		for (String endPoint : authenticationEndPoints) {
			try {
				logger.debug("[obtain] Try to get token at " + endPoint);
				result = httpClient.resource(endPoint).path("/token").type(MediaType.APPLICATION_FORM_URLENCODED).
					post(TokenResponse.class, form);
			} catch (Exception exc) {
				// No response returned.
				logger.debug("[obtain] Unable to get token : " + exc.getMessage(), exc);
			}
			// Leaves at first result.
			if (result != null) {
				logger.debug("[obtain] Token found at " + endPoint);
				break;
			}
		}
		if (result == null) {
			logger.trace("[obtain] No token found !");
			throw new NoTokenFoundException("No token obtained for resource '" + resource + "' and scope '" + scope + 
					"'");
		}
		return result;
	} // obtain().
	
	/**
	 * Retrieves an access token for a given protected resource, and stores this token.<br/>
	 * An existing token for this resource with the same scope will be erased.
	 * 
	 * @param resource The resource for whom an access token is added.
	 * @param token Values (access, refresh, scope) for this token.
	 */
	public void add(String resource, TokenResponse token) {
		List<TokenResponse> known = new ArrayList<TokenResponse>();
		if (tokens.containsKey(resource)) {
			known = tokens.get(resource);
		}
		boolean replaced = false;
		for (TokenResponse candidate : known) {
			// We found another token with the same scope.
			if ((candidate.scope == null && token.scope == null) || 
					candidate.scope.equals(token.scope)) {
				logger.trace("[add] For resource '" + resource + "', replace old token " + candidate.toString() + 
						" with new one " + token.toString());
				// Replace the old value with the new one.
				known.set(known.indexOf(candidate), token);
				replaced = true;
				break;
			}
		}
		if (!replaced) {
			logger.trace("[add] Add token " + token.toString() + " for resource '" + resource + "'");
			known.add(token);
		}
		// Update the map.
		tokens.put(resource, known);
	} // add().
	
	/**
	 * Returns existing tokens for a given resource.
	 * 
	 * @param resource The resource concerned by the returned access tokens.
	 * @return A list (never null, but possibly empty) of access tokens.
	 */
	public List<TokenResponse> consult(String resource) {
		List<TokenResponse> known = new ArrayList<TokenResponse>();
		if (tokens.containsKey(resource)) {
			known = tokens.get(resource);
		}
		return known;
	} // consult().
	
	/**
	 * Enrich a Jersy client HTTP request with the corresponding token.<br/>
	 * Accordingly to the path, sets the suitable token.<br/><br/>
	 * 
	 * Token retrieval is done:
	 * <ol><li>Search in the path names of known resources</li>
	 * <li>If found, all that remains after the resource's name is extracted as "computed scope"</li>
	 * <li>For the given resource, search the tokens whose scope is the begining of "computed scope"</li>
	 * <li>Token whose scope is the longest is applied</li></ol>
	 * 
	 * This way, the right token is applied, even for empty scopes.
	 * @param request The WebResource to enrich with access token.
	 * @return The enriched WebResource.
	 * 
	 * @throws NoTokenFoundException If no token can be found for this resource and scope.
	 */
	public Builder enrich(WebResource request) throws NoTokenFoundException {
		Builder result = request.getRequestBuilder();
		TokenResponse token = null;

		// Extract resource name and computed scope from url.
		String url = request.getURI().getPath();
		int idx = url.indexOf("/", 1);
		String resourceName = idx != -1 ? url.substring(0,idx) : url;
		String computedScope = idx != -1 ? url.substring(resourceName.length()) : null;
		
		// If we already have tokens for this resource.
		if (tokens.contains(resourceName)) {
			List<TokenResponse> knownTokens = tokens.get(resourceName);
			for (TokenResponse candidate : knownTokens) {
				// If we found a relevant scope.
				if ((computedScope == null && candidate.scope == null) || computedScope.startsWith(candidate.scope)) {
					token = candidate;
					// Just leave.
					break;
				}
			}
		}
		if(token == null) {
			logger.debug("[enrich] Try to automatically find a token for '" + resourceName + "' with scope '" + 
					computedScope + "'");
			// No token found, try to find one.
			token = obtain(resourceName, computedScope);
			if (token != null) {
				add(resourceName, token);
			} else {
				logger.debug("[enrich] No relevant token found for '" + url + "'");
			}
		}
		// Enrich the request.
		if (token != null) {
			logger.debug("[enrich] Token " + token.toString() + " found for '" + url + "'");			
			result = request.header(HttpHeaders.AUTHORIZATION, "OAuth "+token.accessToken);
		}
		return result;
	} // enrich().
	
} // class TokenRepository.
