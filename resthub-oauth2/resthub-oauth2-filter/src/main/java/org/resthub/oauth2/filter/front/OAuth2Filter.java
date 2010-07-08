package org.resthub.oauth2.filter.front;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response.Status;

import org.resthub.oauth2.filter.service.ValidationService;
import org.resthub.oauth2.provider.exception.ProtocolException;
import org.resthub.oauth2.provider.model.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

/**
 * OAuth 2 Servlet filter to protect resources on a Resource server.
 * <ul>
 * <li>Extract the access token from the request</li>
 * <li>Gets the corresponding information with the Authorization server</li>
 * <li>Validate life time and scope</li>
 * <li>Process if possible the request.</li>
 * </ul>
 */
@Named("oauth2Filter")
public class OAuth2Filter implements Filter {

	// -----------------------------------------------------------------------------------------------------------------
	// Private attributes
	
	/**
	 * Class logger.
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * The validation service. managed by Spring.
	 */
	@Inject
	protected ValidationService service;
	
	/**
	 * Resource protected and served by this server.
	 */
	@Value("#{securityConfig.resourceName}")
	protected String resource = "";
	
	// -----------------------------------------------------------------------------------------------------------------
	// Protected methods

	/**
	 * Sets the WWW-Authenticate response header to explain the reject of an incoming request.
	 * 
	 * @param response The HTTP Response, enriched with header.
	 * @param error The error case.
	 * @param description The error description.
	 * @param errorStatus The HTTP response code.
	 */
	protected void setError(HttpServletResponse response, String error, String description, Status errorStatus) {
		 StringBuilder sb = new StringBuilder();
		 sb.append("Token realm=\"").
		 	append(resource).
		 	append("\"");
		 if(error != null) {
			 sb.append(", error=\"").append(error).append("\"");
			 if(description != null) {
				 sb.append(", error-description=\"").append(description).append("\"");
			 }
		 }
		 // Sets the autication header.
		 response.setStatus(errorStatus.getStatusCode());
		 response.setHeader(HttpHeaders.WWW_AUTHENTICATE, sb.toString());
	} // setError().
	
	// -----------------------------------------------------------------------------------------------------------------
	// Public Filter inherrited methods

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init(FilterConfig config) throws ServletException {
		// Emtpy
		logger.trace("[init] OAuth 2 filter initialization");
		
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
	public void doFilter(ServletRequest rawRequest, ServletResponse rawResponse,
			FilterChain chain) throws IOException, ServletException {
		
		// Only for HTTP requests.
		if(rawRequest instanceof HttpServletRequest && rawResponse instanceof HttpServletResponse) {
			HttpServletRequest request = (HttpServletRequest)rawRequest;
			HttpServletResponse response = (HttpServletResponse)rawResponse;
			
			logger.trace("[doFilter] Filters request {}", request.getRequestURL());
			String headerValue = request.getHeader(HttpHeaders.AUTHORIZATION);
			try {
				// Token validation
				Token token = service.validateToken(headerValue);
				// Process request.
				chain.doFilter(new SecuredHttpRequest(token.userId, token.permissions, request), rawResponse);
			} catch (ProtocolException exc) {
				logger.trace("[doFilter] Cannot process request", exc);
				setError(response, exc.errorCase.value(), exc.getMessage(), exc.errorCase.status());			
			} catch (Exception exc) {
				logger.warn("[doFilter] Cannot process request for {}: {}", request.getRequestURL(), exc.getMessage());
				logger.warn("[doFilter] Cause: ", exc);
				setError(response, null, null, Status.INTERNAL_SERVER_ERROR);			
			}
		}
	} // doFilter().

} // class OAuth2Filter.
