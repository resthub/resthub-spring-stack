package org.resthub.oauth2.httpclient;

import java.io.IOException;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.protocol.HttpContext;

public class OAuth2Interceptor implements HttpRequestInterceptor {
	
	private final OAuth2Credentials credentials;

	public OAuth2Interceptor(OAuth2Credentials credentials) {
		this.credentials = credentials;
	}

	@Override
	public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
		AuthState authState = (AuthState)context.getAttribute(ClientContext.TARGET_AUTH_STATE);
		if (authState != null && authState.getAuthScheme() == null)
		{
			AuthScheme scheme = new OAuth2SchemeFactory().newInstance(new BasicHttpParams());
			Credentials cred = credentials;
			authState.setAuthScheme(scheme);
			authState.setCredentials(cred);
		}
	}
}
