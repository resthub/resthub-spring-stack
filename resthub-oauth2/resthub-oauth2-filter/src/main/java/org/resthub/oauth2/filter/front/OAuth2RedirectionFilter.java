package org.resthub.oauth2.filter.front;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.resthub.oauth2.common.front.model.TokenResponse;
import org.resthub.oauth2.filter.service.TokenProcessingService;
import org.resthub.web.jackson.JacksonProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.representation.Form;

/**
 * <b>--Part of the Web-Server Profile--</b><br/><br/>
 * 
 * Deployed on a Web-Server resource, this filter redirects incoming request to the authorization end-point.<br/>
 * User is authenticated, and its user-agent redirected on this filter in return, with an access code.<br/>
 * The filter then request the corresponding user's details, and proceed with authentication.<br/><br/>
 * 
 * To be independant from any security toolkit, this filter is not indended to be mapped on the whole protected site.<br/>
 * Therefore all incoming request will be redirected to the authorization server. The security toolkit used is intented
 * to redirect rejected request to this url.<br/><br/>
 * 
 * Only GET incoming requests are processed, because authorization server won't be able to redirect back with another 
 * method.
 */
public class OAuth2RedirectionFilter implements Filter {

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
	 * end-point"). Value read in servlet configuration: init-parameter "authorizationServer".
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
	 * Name of the query parameter that contains the targeted url. Used as callback by the Authorization server.
	 * Value read in servlet configuration: init-parameter "targetUrlParameter".
	 */
	protected String targetUrlParameter = "";

	/**
	 * Used to inject the name of the query parameter that contains the targeted url when working with spring.
	 * 
	 * @param targetUrlParameter The injected query parameter name.
	 */
	public void setTargetUrlParameter(String targetUrlParameter) {
		this.targetUrlParameter = targetUrlParameter;
	} // setTargetUrlParameter().

	/**
	 * Client Id used to communicate with the Authorization server.
	 * Value read in servlet configuration: init-parameter "clientId".
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
	 * Value read in servlet configuration: init-parameter "clientSecret".
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
	 * The service use to process incoming tokens. The implementation class for this class is specified in servlet 
	 * configuration: init-parameter "tokenProcessingServiceClass". This class must implements the
	 * TokenProcessingService interface and have a default constructor.
	 */
	protected TokenProcessingService service;

	/**
	 * Used to inject the service implementation when working with spring.
	 * 
	 * @param service The injected service.
	 */
	public void setService(TokenProcessingService service) {
		this.service = service;
	} // setService().

	// -----------------------------------------------------------------------------------------------------------------
	// Protected methods

	/**
	 * Performs an HTTP request to the authorization server with code, 
	 */
	protected void obtainToken(String code, HttpServletRequest request, HttpServletResponse response) {
		logger.debug("[obtainToken] Get token from code {}", code);
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
			logger.trace("[obtainToken] Send request to {}...", authorizationServer);
			tokenResponse = connector.path("token").type(MediaType.APPLICATION_FORM_URLENCODED).
					post(TokenResponse.class, form);
			logger.trace("[obtainToken] Process response...");
			service.process(tokenResponse, request.getParameter("state"), request, response);
			logger.debug("[obtainToken] User authentified !");
		} catch (Exception exc) {
			logger.warn("[obtainToken] Cannot retrieve token from code: " + exc.getMessage(), exc);
			// Token no accessible
			response.setStatus(Status.INTERNAL_SERVER_ERROR.getStatusCode());
		}
	} // obtainToken().

	// -----------------------------------------------------------------------------------------------------------------
	// Constructor;
	
	/**
	 * Default constructor. Initialize the web client.
	 */
	public OAuth2RedirectionFilter() {
		logger.trace("[Constructor] REST WS client initialization");
		ClientConfig config = new DefaultClientConfig();
	    config.getSingletons().add(new JacksonProvider());
	    wsClient = Client.create(config);
	} // Constructor.

	// -----------------------------------------------------------------------------------------------------------------
	// Filter inherited methods
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init(FilterConfig config) throws ServletException {
		logger.trace("[init] OAuth 2 redirection filter initialization");
		setAuthorizationServer(config.getInitParameter("authorizationServer"));
		setTargetUrlParameter(config.getInitParameter("targetUrlParameter"));
		setClientId(config.getInitParameter("clientId"));
		setClientId(config.getInitParameter("clientSecret"));
		// Creates a processing service.
		String tokenProcessingServiceClass = config.getInitParameter("tokenProcessingServiceClass");
		try {
			setService((TokenProcessingService) Class.forName(tokenProcessingServiceClass).newInstance());
		} catch (Exception exc) {
			String error = "Unable to instanciate the TokenProcessingService class '" + 
					tokenProcessingServiceClass + "' : " + exc.getMessage();
			logger.error("[init] " + error);
			throw new ServletException(error, exc);
		}
		logger.trace("[init] OAuth 2 redirection filter initialized !");
	} // init().
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void destroy() {
		logger.trace("[destroy] OAuth 2 redirection filter destruction");
	} // destroy().
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException,
			ServletException {
		// Redirect incoming requests.
		if (req instanceof HttpServletRequest && resp instanceof HttpServletResponse) {
			HttpServletRequest request = (HttpServletRequest) req;
			HttpServletResponse response = (HttpServletResponse) resp;
			if (request.getMethod() != HttpMethod.GET) {
				// NOT_ALLOWED
				response.setStatus(405);
			} else {
				// Gets the wanted url
				String targetUrl = request.getParameter(targetUrlParameter);
				String code = request.getParameter("code");
				if(targetUrl == null && code == null) {
					response.setStatus(Status.BAD_REQUEST.getStatusCode());
				} else if (targetUrl != null) {
					// No access code, lets redirect to the authentication server.
					// Build redirection url.
					StringBuilder redirection = new StringBuilder(authorizationServer)
							.append("?response_type=code&client_id=")
							.append(clientId)
							.append("&redirect_uri=")
							.append(URLEncoder.encode(request.getRequestURL().toString(), "UTF-8"))
							.append("&state=")
							.append(URLEncoder.encode(targetUrl, "UTF-8"));
					logger.trace("[doFilter] redirection to {}", redirection.toString());
					// Redirect incoming request.
					response.sendRedirect(redirection.toString());
				} else if (code != null) {
					// Access code: lets get the corresponding token.
					obtainToken(code, request, response);
				}
			}
		} else {
			// Process existing requests.
			chain.doFilter(req, resp);
		}
	} // doFilter().
	
} // class OAuth2RedirectionFilter.
