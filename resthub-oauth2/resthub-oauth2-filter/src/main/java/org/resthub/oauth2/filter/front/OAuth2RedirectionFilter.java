package org.resthub.oauth2.filter.front;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Response.Status;

import org.resthub.oauth2.common.front.model.TokenResponse;
import org.resthub.oauth2.common.model.Token;
import org.resthub.oauth2.filter.service.TokenProcessingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * method.<br/><br/>
 * 
 * Once the token and the corresponding User details were retrieved, a TokenProcessingService is invoked to make 
 * specific treatments.<br/><br/>
 * 
 * This filter could be declared as clasical Servlet filter, and needs the following initial parameters:<br/>
 * Web.xml
 * <pre>
 * &lt;filter&gt;
 *     &lt;!-- The name is important (thanks to Spring mecanisms). It's the filter bean name --&gt;
 *     &lt;filter-name&gt;OAuth2Filter&lt;/filter-name&gt;
 *     &lt;filter-class&gt;org.springframework.web.filter.DelegatingFilterProxy&lt;/filter-class&gt;
 *     &lt;init-param&gt;
 *         &lt;param-name&gt;authorizationServer&lt;/param-name&gt;
 *         &lt;param-value&gt;http://localhost:8080/identity/api/authorize&lt;/param-value&gt;
 *     &lt;/init-param&gt;
 *     &lt;init-param&gt;
 *         &lt;param-name&gt;targetUrlParameter&lt;/param-name&gt;
 *         &lt;param-value&gt;requested&lt;/param-value&gt;
 *     &lt;/init-param&gt;
 *     &lt;init-param&gt;
 *         &lt;param-name&gt;clientId&lt;/param-name&gt;
 *         &lt;param-value&gt;&lt;/param-value&gt;
 *     &lt;/init-param&gt;
 *     &lt;init-param&gt;
 *         &lt;param-name&gt;clientSecret&lt;/param-name&gt;
 *         &lt;param-value&gt;&lt;/param-value&gt;
 *     &lt;/init-param&gt;
 *     &lt;init-param&gt;
 *         &lt;param-name&gt;accessTokenParam&lt;/param-name&gt;
 *         &lt;param-value&gt;access_token&lt;/param-value&gt;
 *     &lt;/init-param&gt;
 *     &lt;init-param&gt;
 *         &lt;param-name&gt;authorizationPassword&lt;/param-name&gt;
 *         &lt;param-value&gt;p@ssw0rd&lt;/param-value&gt;
 *     &lt;/init-param&gt;
 *     &lt;init-param&gt;
 *         &lt;param-name&gt;tokenProcessingServiceClass&lt;/param-name&gt;
 *         &lt;param-value&gt;org.mycompany.security.MyTokenProcessingService&lt;/param-value&gt;
 *     &lt;/init-param&gt;
 * &lt;/filter&gt;
 * &lt;filter-mapping&gt;
 *     &lt;filter-name&gt;OAuth2Filter&lt;/filter-name&gt;
 *     &lt;!-- Here the path of your protected resource --&gt;
 *     &lt;url-pattern&gt;/yourProtectedPath&lt;/url-pattern&gt;
 * &lt;/filter-mapping&gt;
 * </pre>
 * <b>OR</b> it could be declared as Bean filter with the spring's Delegatingfilter. Parameters are injected:<br/>
 * Web.xml
 * <pre>
 * &lt;filter&gt;
 *     &lt;!-- The name is important (thanks to Spring mecanisms). It's the filter bean name --&gt;
 *     &lt;filter-name&gt;OAuth2Filter&lt;/filter-name&gt;
 *     &lt;filter-class&gt;org.springframework.web.filter.DelegatingFilterProxy&lt;/filter-class&gt;
 * &lt;/filter&gt;
 * &lt;filter-mapping&gt;
 *     &lt;filter-name&gt;OAuth2Filter&lt;/filter-name&gt;
 *     &lt;!-- Here the path of your protected resource --&gt;
 *     &lt;url-pattern&gt;/yourProtectedPath&lt;/url-pattern&gt;
 * &lt;/filter-mapping&gt;
 * </pre>
 * ApplicationContext.xml
 * <pre>
 * &lt;bean name="OAuth2Filter" class="org.resthub.oauth2.filter.front.OAuth2Filter"&gt;
 *     &lt;property name="authorizationServer" value="#{securityConfig.authorizationServer}" /&gt;
 *     &lt;property name="targetUrlParameter" value="#{securityConfig.targetUrlParameter}" /&gt;
 *     &lt;property name="clientId" value="#{securityConfig.clientId}" /&gt;
 *     &lt;property name="clientSecret" value="#{securityConfig.clientSecret}" /&gt;
 *     &lt;property name="accessTokenParam" value="#{securityConfig.accessTokenParam}" /&gt;
 *     &lt;property name="authorizationPassword" value="#{authorizationPassword}" /&gt;
 *     &lt;property name="tokenProcessingService" ref="myProcessingService" /&gt;
 * &lt;/bean&gt;
 * </pre>
 */
public class OAuth2RedirectionFilter implements Filter {

	// -----------------------------------------------------------------------------------------------------------------
	// Private attributes

	/**
	 * Class logger.
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Protocol utilities for Web-Server profile implementation.
	 */
	protected WebServerUtilities utilities;

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
		utilities.setAuthorizationServer(authorizationServer);
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
		utilities.setClientId(clientId);
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
		utilities.setClientSecret(clientSecret);
	} // setClientSecret().

	/**
	 * Name of the query parameter that contains the access tokent when retrieving the token details.
	 * Value read in servlet configuration: init-parameter "accessTokenParam".
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
		utilities.setAccessTokenParam(accessTokenParam);
	} // setAccessTokenParam().
	
	/**
	 * Password used to get token information on the central authorization service.
	 * Value read in servlet configuration: init-parameter "authorizationPassword".
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
		utilities.setAuthorizationPassword(authorizationPassword);
	} // setAuthorizationPassword().

	/**
	 * The service use to process incoming tokens. The implementation class for this class is specified in servlet 
	 * configuration: init-parameter "tokenProcessingServiceClass". This class must implements the
	 * TokenProcessingService interface and have a default constructor.
	 */
	protected TokenProcessingService tokenProcessingService;

	/**
	 * Used to inject the service implementation when working with spring.
	 * 
	 * @param tokenProcessingService The injected service.
	 */
	public void setTokenProcessingService(TokenProcessingService tokenProcessingService) {
		this.tokenProcessingService = tokenProcessingService;
	} // setTokenProcessingService().

	// -----------------------------------------------------------------------------------------------------------------
	// Constructor;
	
	/**
	 * Default constructor. Initialize the web client.
	 */
	public OAuth2RedirectionFilter() {
		logger.trace("[Constructor] REST WS client initialization");
		utilities = new WebServerUtilities();
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
		setClientSecret(config.getInitParameter("clientSecret"));
		setAuthorizationPassword(config.getInitParameter("authorizationPassword"));
		setAccessTokenParam(config.getInitParameter("accessTokenParam"));
		// Creates a processing service.
		String tokenProcessingServiceClass = config.getInitParameter("tokenProcessingServiceClass");
		try {
			setTokenProcessingService((TokenProcessingService) Class.forName(tokenProcessingServiceClass).newInstance());
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
				try {
					if(targetUrl == null && code == null) {
						response.setStatus(Status.BAD_REQUEST.getStatusCode());
					} else if (code != null) {
						// Access code: lets get the corresponding token.
						TokenResponse token = utilities.obtainTokenFromCode(code, request, response);
						Token tokenDetails = utilities.obtainTokenDetails(token.accessToken);
						tokenProcessingService.process(tokenDetails, request.getParameter("state"), request, response);
					} else if (targetUrl != null) {
						// No access code, lets redirect to the authentication server.
						// Build redirection url.
						utilities.redirectToAuthenticationEndPoint(targetUrl, request, response, true);
					} 
				} catch (Exception exc) {
					// Error when processing request.
					response.setStatus(Status.INTERNAL_SERVER_ERROR.getStatusCode());
				}
			}
		} else {
			// Process existing requests.
			chain.doFilter(req, resp);
		}
	} // doFilter().
	
} // class OAuth2RedirectionFilter.
