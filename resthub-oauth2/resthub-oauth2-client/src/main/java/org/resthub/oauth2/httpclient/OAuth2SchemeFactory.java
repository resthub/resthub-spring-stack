package org.resthub.oauth2.httpclient;

import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthSchemeFactory;
import org.apache.http.params.HttpParams;

public class OAuth2SchemeFactory implements AuthSchemeFactory {
	
    public static final String SCHEME_NAME = "OAuth2";

    public AuthScheme newInstance(final HttpParams params) {
        return new OAuth2Scheme();
    }
}
