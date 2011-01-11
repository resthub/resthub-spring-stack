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
import javax.ws.rs.core.Response.Status;

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
 * method.
 */
public class OAuth2RedirectionFilter implements Filter {

	// -----------------------------------------------------------------------------------------------------------------
	// Private attributes

	/**
	 * Class logger.
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());

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
	 * @param authorizationServer
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
	 * @param targetUrlParameter
	 */
	public void setTargetUrlParameter(String targetUrlParameter) {
		this.targetUrlParameter = targetUrlParameter;
	} // setTargetUrlParameter().

	/**
	 * Client Id used to communicate with the Authorization server.
	 * Value read in servlet configuration: init-parameter "clientId".
	 */
	protected String clientId = "";

	/**
	 * Used to inject the client id when working with spring.
	 * 
	 * @param targetUrlParameter
	 */
	public void setClientId(String clientId) {
		this.clientId = clientId;
	} // setClientId().

	// -----------------------------------------------------------------------------------------------------------------
	// Protected methods


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
	} // init().
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void destroy() {
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
				if(targetUrl == null) {
					response.setStatus(Status.BAD_REQUEST.getStatusCode());
				} else {
					// Build redirection url.
					String redirection = authorizationServer+"?response_type=code&client_id="+clientId+"&redirect_uri="+
							URLEncoder.encode(targetUrl, "UTF-8");
					logger.trace("[doFilter] redirection to {}", redirection);
					// Redirect incoming request.
					response.sendRedirect(redirection);
				}
			}
		} else {
			// Process existing requests.
			chain.doFilter(req, resp);
		}
	} // doFilter().
	
} // class OAuth2RedirectionFilter.
