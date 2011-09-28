package org.resthub.oauth2.httpclient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.auth.RFC2617Scheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.resthub.web.SerializationHelper;

public class OAuth2Scheme extends RFC2617Scheme {

    // Parameter names
    public static final String GRANT_TYPE_PARAMETER_NAME = "grant_type";
    public static final String CLIENT_ID_PARAMETER_NAME = "client_id";
    public static final String CLIENT_SECRET_PARAMETER_NAME = "client_secret";
    public static final String USERNAME_PARAMETER_NAME = "username";
    public static final String PASSWORD_PARAMETER_NAME = "password";
    public static final String SCOPE_PARAMETER_NAME = "scope";

    public static final String PASSWORD_PARAMETER_VALUE = "password";

    private enum AuthState {
        UNINITIATED, CHALLENGE_RECEIVED, TOKEN_GENERATED, FAILED
    };

    private AuthState state = AuthState.UNINITIATED;

    OAuth2Scheme() {
    }

    @Override
    // Refer to the following URL to understand why scope != realm
    // http://www.ietf.org/mail-archive/web/oauth/current/msg01657.html
    public String getRealm() {
        return null;
    }

    @Override
    public String getSchemeName() {
        return OAuth2SchemeFactory.SCHEME_NAME;

    }

    @Override
    public boolean isComplete() {
        return this.state == AuthState.TOKEN_GENERATED;
    }

    @Override
    public boolean isConnectionBased() {
        return false;
    }

    @Override
    public void processChallenge(Header challenge) throws MalformedChallengeException {
        super.processChallenge(challenge);

        String error = this.getParameter("error");

        if ("invalid_token".equals(error)) {
            // The token may be invalid, so we have to challenge the token
            // again.
            this.state = AuthState.CHALLENGE_RECEIVED;
        } else {
            this.state = AuthState.FAILED;
        }
    }

    @Override
    public Header authenticate(Credentials credentials, HttpRequest httpRequest) throws AuthenticationException {
        if (credentials == null) {
            throw new AuthenticationException("Credentials should not be null !");
        }
        if (!(credentials instanceof OAuth2Credentials)) {
            throw new AuthenticationException(
                    "Credentials should be an instance of OAuth2Credentials but credential parameter provided is "
                            + credentials.getClass().getSimpleName());
        }
        OAuth2Credentials oauth2Credentials = (OAuth2Credentials) credentials;

        if (oauth2Credentials.getTokenResponse() == null || this.state == AuthState.CHALLENGE_RECEIVED) {
            HttpClient httpClient = new DefaultHttpClient();

            // Allow unsigned SSL certificates for testing purpose
            // TODO : control this thanks to a properties value
            SSLSocketFactory sf = (SSLSocketFactory) httpClient.getConnectionManager().getSchemeRegistry()
                    .getScheme("https").getSchemeSocketFactory();
            sf.setHostnameVerifier(new AllowAllHostnameVerifier());

            HttpPost authenticateRequest = new HttpPost(oauth2Credentials.getAuthenticationEndPoint());

            // Copy params (proxy, certificate validation strategy ...) from
            // sent request to authentication request
            authenticateRequest.setParams(httpRequest.getParams());

            authenticateRequest.setHeader("Content-Type", "application/x-www-form-urlencoded");
            authenticateRequest.setHeader("Accept", "application/json");

            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair(GRANT_TYPE_PARAMETER_NAME, PASSWORD_PARAMETER_VALUE));
            parameters.add(new BasicNameValuePair(CLIENT_ID_PARAMETER_NAME, oauth2Credentials.getClientId()));
            parameters.add(new BasicNameValuePair(CLIENT_SECRET_PARAMETER_NAME, oauth2Credentials.getClientSecret()));
            parameters.add(new BasicNameValuePair(USERNAME_PARAMETER_NAME, oauth2Credentials.getUserPrincipal()
                    .getName()));
            parameters.add(new BasicNameValuePair(PASSWORD_PARAMETER_NAME, oauth2Credentials.getPassword()));
            parameters.add(new BasicNameValuePair(SCOPE_PARAMETER_NAME, oauth2Credentials.getScope()));

            try {
                UrlEncodedFormEntity sentEntity = new UrlEncodedFormEntity(parameters, HTTP.UTF_8);
                authenticateRequest.setEntity(sentEntity);
                HttpResponse response = httpClient.execute(authenticateRequest);
                String responseEntity = EntityUtils.toString(response.getEntity());
                OAuth2TokenResponse tokenResponse = (OAuth2TokenResponse) SerializationHelper.jsonDeserialize(
                        responseEntity, OAuth2TokenResponse.class);
                if (tokenResponse == null) {
                    throw new AuthenticationException("Unable to parse token reponse !");
                }
                oauth2Credentials.setTokenResponse(tokenResponse);
                this.state = AuthState.TOKEN_GENERATED;
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Since a new OAuth2Scheme is created on each request, if we
            // already have a token, make it so.
            this.state = AuthState.TOKEN_GENERATED;
        }

        return new BasicHeader(HttpHeaders.AUTHORIZATION, this.getSchemeName() + " "
                + oauth2Credentials.getTokenResponse().accessToken);
    }

}
