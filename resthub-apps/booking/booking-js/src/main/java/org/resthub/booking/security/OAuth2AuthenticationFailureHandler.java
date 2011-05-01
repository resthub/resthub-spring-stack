package org.resthub.booking.security;

import java.io.IOException;

import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Named("oauth2AuthenticationFailureHandler")
public class OAuth2AuthenticationFailureHandler implements
		AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException ex)
			throws IOException, ServletException {
		response.sendError(HttpServletResponse.SC_FORBIDDEN);

	}

}
