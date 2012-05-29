package org.resthub.web.oauth2;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response of the token end-point, as described in the Oauth 2 specification (Section 4.2).
 */
public class OAuth2Token {

    /**
     * The access token issued by the authorization server.
     */
    protected String accessToken;
    /**
     * The duration in seconds of the access token lifetime.
     */
    protected Integer expiresIn;
    /**
     * The refresh token used to obtain new access tokens using the same end-user access grant.
     */
    protected String refreshToken;
    /**
     * The scope of the access token as a list of space-delimited strings. The value of the "scope" parameter is defined
     * by the authorization server. If the value contains multiple space-delimited strings, their order does not matter,
     * and each string adds an additional access range to the requested scope.
     */
    protected String scope;

    @JsonProperty("access_token")
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @JsonProperty("expires_in")
    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    @JsonProperty("refresh_token")
    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @JsonProperty("scope")
    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return new StringBuilder("[Token] access token: ").append(accessToken).append(" expires in: ")
                .append(expiresIn).append(" refresh token: ").append(refreshToken).append(" scope: ").append(scope)
                .toString();
    }
}
