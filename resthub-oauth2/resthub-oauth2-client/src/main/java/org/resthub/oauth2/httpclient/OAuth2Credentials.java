package org.resthub.oauth2.httpclient;

import org.apache.http.auth.UsernamePasswordCredentials;

public class OAuth2Credentials extends UsernamePasswordCredentials {

	private static final long serialVersionUID = 7599200855676581940L;
	
	private String clientId = "";
	private String clientSecret = "";
	private String grantType = "";
	private String scope = "";
	private String authenticationEndPoint = "";

	private OAuth2TokenResponse tokenResponse = null;
		
	public OAuth2Credentials(String authenticationEndPoint, String username, String password) {
		super(username, password);
		this.authenticationEndPoint = authenticationEndPoint;
	}
	
	public OAuth2Credentials(String authenticationEndPoint, String clientId, String username, String password) {
		super(username, password);
		this.clientId = clientId;
		this.authenticationEndPoint = authenticationEndPoint;
	}
	
	public OAuth2Credentials(String authenticationEndPoint, String clientId, String clientSecret, String username, String password) {
		super(username, password);
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.authenticationEndPoint = authenticationEndPoint;
	}
	
	public OAuth2TokenResponse getTokenResponse() {
		return tokenResponse;
	}

	public void setTokenResponse(OAuth2TokenResponse tokenResponse) {
		this.tokenResponse = tokenResponse;
	}
	
	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	
	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getGrantType() {
		return grantType;
	}

	public void setGrantType(String grantType) {
		this.grantType = grantType;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}
	
	public String getAuthenticationEndPoint() {
		return authenticationEndPoint;
	}

	public void setAuthenticationEndPoint(String authenticationEndPoint) {
		this.authenticationEndPoint = authenticationEndPoint;
	}
	
}
