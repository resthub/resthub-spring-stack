package org.resthub.oauth2.client;

import org.apache.http.auth.AuthScope;
import org.apache.http.impl.client.DefaultHttpClient;
import org.resthub.oauth2.httpclient.OAuth2Credentials;
import org.resthub.oauth2.httpclient.OAuth2Interceptor;
import org.resthub.oauth2.httpclient.OAuth2SchemeFactory;
import org.resthub.web.client.ClientFactory;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.client.apache4.ApacheHttpClient4;

public class OAuth2ClientFactory {

	public static Client create(OAuth2Credentials credentials) {
    	ApacheHttpClient4 jerseyClient = ClientFactory.create();
    	DefaultHttpClient httpClient = (DefaultHttpClient)jerseyClient.getClientHandler().getHttpClient();
    	
    	httpClient.getAuthSchemes().register(OAuth2SchemeFactory.SCHEME_NAME, new OAuth2SchemeFactory());
    	httpClient.getCredentialsProvider().setCredentials(new AuthScope(AuthScope.ANY), credentials);
    	httpClient.addRequestInterceptor(new OAuth2Interceptor(credentials), 0);

        return jerseyClient;
    }
}
