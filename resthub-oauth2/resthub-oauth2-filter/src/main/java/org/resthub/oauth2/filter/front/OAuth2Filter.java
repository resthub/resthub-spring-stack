package org.resthub.oauth2.filter.front;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.resthub.oauth2.common.exception.ProtocolException.Error;
import org.resthub.oauth2.common.model.Token;
import org.resthub.oauth2.filter.service.ValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OAuth 2 Servlet filter to protect resources on a Resource server.
 * <ul>
 * <li>Extract the access token from the request</li>
 * <li>Gets the corresponding information with the Authorization server</li>
 * <li>Validate life time and scope</li>
 * <li>Process if possible the request.</li>
 * </ul>
 * 
 * For protected resource that are not accessed by a rich client, the OAuth2 token cannot be added to each incomming 
 * request.
 * The "DoSetCookie" mode stores the validated token into a cookie.
 * When no token is specified by header or as GET/POST parameter, it reuse the existing cookie.
 * 
 * This filter can populate the Spring Security's SecurityContext if you want to use Spring Security ACLs.
 * <b><u>The Spring-Security artifact need to be provided at runtime</u>. (groupId: org.springframework.security, 
 * artifactId: spring-security-acl, version: 3.0.5.RELEASE+)</b><br/><br/>
 *
 * This filter can be declared with Spring with a DelegatingFilterProxy, and an XML declaration :
 * <br/>In web.xml
 * <pre>
 * &lt;filter&gt;
 *   &lt;!-- The name is important (thanks to Spring mecanisms). It's the filter bean name	--&gt;
 *   &lt;filter-name&gt;OAuth2Filter&lt;/filter-name&gt;
 *   &lt;filter-class&gt;org.springframework.web.filter.DelegatingFilterProxy&lt;/filter-class&gt;
 * &lt;/filter&gt;
 * </pre>
 * <br/>In spring configuration files
 * <pre>
 * &lt;!-- Declaration of the filter --&gt;
 * &lt;bean name="OAuth2Filter" class="org.resthub.oauth2.filter.front.OAuth2Filter"&gt;
 *   &lt;property name="service" ref="validationService" /&gt;
 *   &lt;property name="resource" value="MyResource" /&gt;
 *   &lt;property name="doSetCookie" value="false" /&gt;
 *   &lt;property name="popupateSecurityContext" value="true" /&gt;
 * &lt;/bean&gt;
 * </pre>
 * 
 * Or this filter can be parametrized only in the Web.xml. You'll have to set the following parameters :
 * <ul>
 * <li>securedResourceName: Resource protected and served by this server</li>
 * <li>doSetCookie: Enable or not the Cookie behaviour (false if absent)</li>
 * <li>validationServiceClass: The validation service class name and package. This class must implements the 
 * ValidationService interface and have a default constructor</li>
 * <li>popupateSecurityContext: Used if the SpringSecurity's SecurityContext needs to be populated with the 
 * * connected user.</li>
 * </ul>
 * In your Web.xml:
 * <pre>
 * &lt;context-param&gt;
 *   &lt;param-name&gtsecuredResourceName&lt;/param-name&gt;
 *   &lt;param-value&gtMyResource&lt;/param-value&gt;
 * &lt;/context-param;
 * &lt;context-param&gt;
 *   &lt;param-name&gtdoSetCookie&lt;/param-name&gt;
 *   &lt;param-value&gtfalse&lt;/param-value&gt;
 * &lt;/context-param;
 * &lt;context-param&gt;
 *   &lt;param-name&gtvalidationServiceClass&lt;/param-name&gt;
 *   &lt;param-value&gtorg.reshub.oauth2.filter.service.ExternalValidationService&lt;/param-value&gt;
 * &lt;/context-param&gt;
 * &lt;context-param&gt;
 *   &lt;param-name&gtpopupateSecurityContext&lt;/param-name&gt;
 *   &lt;param-value&gtrue&lt;/param-value&gt;
 * &lt;/context-param&gt;
 *  
 * &lt;filter&gt;
 *   &lt;!-- The name is important (thanks to Spring mecanisms). It's the filter bean name	--&gt;
 *   &lt;filter-name&gt;OAuth2Filter&lt;/filter-name&gt;
 *   &lt;filter-class&gt;org.resthub.oauth2.filter.front.Oauth2Filter&lt;/filter-class&gt;
 * &lt;/filter&gt;
 * </pre>
 */
public class OAuth2Filter implements Filter {

	// -----------------------------------------------------------------------------------------------------------------
	// Private attributes

	/**
	 * Class logger.
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Constants storing the HTTP parameter name used to extract the access
	 * token.
	 */
	public static final String ACCESSTOKEN_PARAMETER = "oauth_token";

	// -----------------------------------------------------------------------------------------------------------------
	// Public properties

	/**
	 * Resource protected and served by this server. Value read in servlet
	 * configuration: init-parameter "securedResourceName".
	 */
	protected String resource = "";

	/**
	 * Used to inject the service implementation when working with spring.
	 * 
	 * @param service
	 */
	public void setResource(String resource) {
		this.resource = resource;
	} // setResource().

	/**
	 * The validation service. The implementation class for this class is
	 * specified in servlet configuration: init-parameter
	 * "validationServiceClass". This class must implements the
	 * ValidationService interface and have a default constructor.
	 */
	protected ValidationService service;

	/**
	 * Used to inject the service implementation when working with spring.
	 * 
	 * @param service
	 */
	public void setService(ValidationService service) {
		this.service = service;
	} // setService().

	/**
	 * Used if the SpringSecurity's SecurityContext needs to be populated with the 
	 * connected user.
	 */
	protected boolean popupateSecurityContext = false;

	/**
	 * Used to change the popupateSecurityContext option when working with spring.
	 * 
	 * @param service
	 */
	public void setPopupateSecurityContext(Boolean popupateSecurityContext) {
		this.popupateSecurityContext = popupateSecurityContext;
	} // setPopupateSecurityContext().
	
	// -----------------------------------------------------------------------------------------------------------------
	// Protected methods

	/**
	 * Sets the WWW-Authenticate response header to explain the reject of an
	 * incoming request.
	 * 
	 * @param response
	 *            The HTTP Response, enriched with header.
	 * @param error
	 *            The error case.
	 * @param description
	 *            The error description.
	 * @param errorStatus
	 *            The HTTP response code.
	 */
	protected void setError(HttpServletResponse response, String error, String description, Status errorStatus) {
		StringBuilder sb = new StringBuilder();
		sb.append("Token realm=\"").append(resource).append("\"");
		if (error != null) {
			sb.append(", error=\"").append(error).append("\"");
			if (description != null) {
				sb.append(", error_description=\"").append(description).append("\"");
			}
		}
		// Sets the autication header.
		response.setStatus(errorStatus.getStatusCode());
		response.setHeader(HttpHeaders.WWW_AUTHENTICATE, sb.toString());
	} // setError().

	/**
     * Extract from the request an OAuth2 token.<br/>
     * The token can be in the Authorization header, or as parameter (embedded in body or not).
     * If the DoSetCookie mode is enable, search into the request cookies.
     * 
     * If the token is misformated, absent, or present to many times, send errors.
     * 
     * @param request The concerned request
     * @param reponse The concerned response
     * @return The embedd token, or null if no token is available.
     */
    protected String extractToken(HttpServletRequest request, HttpServletResponse response) {
    	String token = null;
    	
    	// Look in headers first
    	String headerValue = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (headerValue != null && !headerValue.matches("^OAuth2 .*$")) {
			// invalid token
			StringBuilder sb = new StringBuilder("The token passed is misformated");
			logger.trace("[doFilter] {}", sb.toString());
			setError(response, Error.INVALID_REQUEST.value(), sb.toString(), Error.INVALID_REQUEST.status());
		} else {
			// Try to extract the accessToken value.
			if (headerValue != null) {
				headerValue = headerValue.replace("OAuth2", "").trim();
			}
			String otherValue = null;
			String method = request.getMethod();
			// Specification says that boty parameter may not be extracted systematically.
			if (request.getHeader(HttpHeaders.CONTENT_TYPE) == MediaType.APPLICATION_FORM_URLENCODED
					&& (method == HttpMethod.POST || method == HttpMethod.DELETE || method == HttpMethod.PUT)) {
				otherValue = request.getParameter(ACCESSTOKEN_PARAMETER);
			} else {
				otherValue = request.getParameter(ACCESSTOKEN_PARAMETER);
			}
			if (headerValue == null && otherValue == null) {
				// No token at all.
				logger.debug("[doFilter] No token found");
				setError(response, Error.UNAUTHORIZED_REQUEST.value(), null, Error.UNAUTHORIZED_REQUEST.status());
			} else if (headerValue != null && otherValue != null) {
				// Too many tokens !
				String error = "More than one method used to exchange token";
				logger.trace("[doFilter] {}", error);
				setError(response, Error.INVALID_REQUEST.value(), error, Error.INVALID_REQUEST.status());
			} else {
				token = headerValue == null ? otherValue : headerValue;
			}
		}
		// Do not returned empty tokens.
		return token != null && token.length() == 0 ? null : token;
    } // extractToken().
    
	// -----------------------------------------------------------------------------------------------------------------
	// Public Filter inherrited methods

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init(FilterConfig config) throws ServletException {
		logger.trace("[init] OAuth 2 filter initialization");
		// Read the service used and the resource name.
		setResource(config.getInitParameter("securedResourceName"));
		String validationServiceClass = config.getInitParameter("validationServiceClass");
		// Creates a validation service.
		try {
			setService((ValidationService) Class.forName(validationServiceClass).newInstance());
		} catch (Exception exc) {
			String error = "Unable to instanciate the ValidationService class '" + 
					validationServiceClass + "' : " + exc.getMessage();
			logger.error("[init] " + error);
			throw new ServletException(error, exc);
		}
		setPopupateSecurityContext(Boolean.valueOf(config.getInitParameter("popupateSecurityContext")));
	} // init().

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void destroy() {
		// Emtpy
		logger.trace("[destroy] OAuth 2 filter finalization");
	} // destroy().

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doFilter(ServletRequest rawRequest, ServletResponse rawResponse, FilterChain chain) throws IOException,
			ServletException {

		// Only for HTTP requests.
		if (rawRequest instanceof HttpServletRequest && rawResponse instanceof HttpServletResponse) {
			HttpServletRequest request = (HttpServletRequest) rawRequest;
			HttpServletResponse response = (HttpServletResponse) rawResponse;

			Token token = null;
			logger.trace("[doFilter] Filters request {}", request.getRequestURL());
			
			// Extract Authorization Header Request.
			String accessToken = extractToken(request, response);
			if (accessToken != null) {
				logger.trace("[doFilter] Accessing with accessToken '{}'", accessToken);
				// Match the token.
				try {
					token = service.validateToken(accessToken);
				} catch (Exception exc) {
					logger.warn("[doFilter] Cannot process request for {}: {}", request.getRequestURL(),
							exc.getMessage());
					logger.warn("[doFilter] Cause: ", exc);
					setError(response, null, null, Status.INTERNAL_SERVER_ERROR);
				}
				if (token == null) {
					// Unknown token
					logger.trace("[doFilter] Unknown token '{}'", accessToken);
					setError(response, Error.INVALID_TOKEN.value(), "Unvalid token", Error.INVALID_TOKEN.status());
				} else {
					// Check token expiration
					Date now = new Date();
					Long expired = (now.getTime() - token.createdOn.getTime()) / 1000;
					if (expired > token.lifeTime) {
						logger.trace("[doFilter] Expired token '{}'", accessToken);
						StringBuilder sb = new StringBuilder("Token has expired ").append(expired - token.lifeTime)
								.append("s ago");
						setError(response, Error.EXPIRED_TOKEN.value(), sb.toString(), Error.EXPIRED_TOKEN.status());
						// Block processing.
						token = null;
					} else {
						// Process request.
						chain.doFilter(new SecuredHttpRequest(token.userId, token.permissions, request), rawResponse);
					}
				}
			}
		}
	} // doFilter().

} // class OAuth2Filter.
