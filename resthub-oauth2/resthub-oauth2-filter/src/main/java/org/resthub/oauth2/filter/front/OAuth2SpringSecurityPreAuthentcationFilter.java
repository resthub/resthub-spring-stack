package org.resthub.oauth2.filter.front;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Response.Status;

import org.resthub.oauth2.common.front.model.TokenResponse;
import org.resthub.oauth2.common.model.Token;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * <b>--Part of the Web-Server Profile--</b><br/><br/>
 * 
 * Deployed on a Web-Server resource managed by Spring-Security, this filter redirects incoming request to the 
 * authorization end-point.<br/>
 * User is authenticated, and its user-agent redirected on this filter in return, with an access code.<br/>
 * The filter then request the corresponding user's details, and proceed with authentication.<br/><br/>
 * 
 * <b><u>The Spring-Security artifact need to be provided at runtime</u>. (groupId: org.springframework.security, 
 * artifactId: spring-security-web, version: 3.0.5.RELEASE+)</b><br/><br/>
 * 
 * This filter must be declared as a PRE_AUTH_FILTER in a Pre-authentication scenario:
 * <pre>
 * &lt;http entry-point-ref="entryPoint"&gt;
 *    &lt;intercept-url pattern="/admin/**" access="ROLE_ADMIN" /&gt;
 *    &lt;custom-filter ref="oauth2RedirectionFilter" position="PRE_AUTH_FILTER"/&gt;
 * &lt;/http&gt;
 * 
 * &lt;b:bean id="entryPoint" class="org.springframework.security.web.authentication.Http403ForbiddenEntryPoint"/&gt;
 *
 * &lt;authentication-manager alias="authenticationManager"&gt;
 *     &lt;authentication-provider ref="preauthAuthProvider"/&gt;
 * &lt;/authentication-manager&gt;
 * &lt;-- to be done: declare preauthAuthProvider bean --&gt;
 * </pre>
 * 
 * And as a been:
 * <pre>    
 * &lt;bean name="oauth2RedirectionFilter" class="org.resthub.oauth2.filter.front.OAuth2SpringSecurityPreAuthentcationFilter"&gt;
 *    &lt;property name="authorizationServer" value="#{securityConfig.authorizationServer}"/&gt;
 *    &lt;property name="targetUrlParameter"  value="#{securityConfig.targetUrlParameter}"/&gt;
 *    &lt;property name="clientId"  value="#{securityConfig.clientId}"/&gt;
 *    &lt;property name="clientSecret"  value="#{securityConfig.clientSecret}"/&gt;
 *    &lt;property name="accessTokenParam" value="#{securityConfig.accessTokenParam}"/&gt;
 *    &lt;property name="authorizationPassword" value="#{securityConfig.authorizationPassword}"/&gt;
 *    &lt;property name="tokenProcessingService" ref="tokenProcessingService"/&gt;	
 * &lt;/bean&gt;
 * </pre>
 * 
 */
public class OAuth2SpringSecurityPreAuthentcationFilter extends OAuth2RedirectionFilter {

	// -----------------------------------------------------------------------------------------------------------------
	// OAuth2RedirectionFilter inherited methods

	private static final String SPRING_SECURITY_AUTH = "SPRING_SECURITY_AUTH";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException,
			ServletException {
		boolean process = true;
		// Redirect incoming requests.
		if (req instanceof HttpServletRequest && resp instanceof HttpServletResponse) {
			HttpServletRequest request = (HttpServletRequest) req;
			HttpServletResponse response = (HttpServletResponse) resp;
			SecurityContext context = SecurityContextHolder.getContext();
			
			if(context.getAuthentication() == null) {
				context = (SecurityContext)req.getAttribute(SPRING_SECURITY_AUTH);
			} else {
				req.setAttribute(SPRING_SECURITY_AUTH, context);
			}
			
			if (request.getMethod() == HttpMethod.GET && context.getAuthentication() == null) {
				process = false;
				// Gets the wanted url
				String targetUrl = request.getRequestURL().toString();
				String code = request.getParameter("code");
				try {
					if (code != null) {
						// Access code: lets get the corresponding token.
						TokenResponse token = utilities.obtainTokenFromCode(code, request, response);
						Token tokenDetails = utilities.obtainTokenDetails(token.accessToken);
						tokenProcessingService.process(tokenDetails, targetUrl, request, response);
						// Redirect to the targeted url.
						response.sendRedirect(targetUrl);
					} else {
						// No access code, lets redirect to the authentication server.
						// Build redirection url.
						utilities.redirectToAuthenticationEndPoint(targetUrl, request, response, false);
					} 
				} catch (Exception exc) {
					// Error when processing request.
					response.setStatus(Status.INTERNAL_SERVER_ERROR.getStatusCode());
				}
			}
		} 
		// Go on to the filtering process
		if (process) {
			chain.doFilter(req, resp);
		}
	} // doFilter().
	
} // Class OAuth2SpringSecurityPreAuthenticationFilter
