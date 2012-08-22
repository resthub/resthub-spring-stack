package org.resthub.web.oauth2;

import org.resthub.web.Client;

/**
 * Configuration class to use with a {@link Client}.
 */
public class OAuth2Config {

    /*
     * User credentials
     */
    protected String username;
    protected String password;
    /*
     * OAuth2.0 client info
     */
    protected String clientId;
    protected String clientSecret;
    /*
     * OAuth2.0 remote endpoints
     */
    protected String accessTokenEndpoint;
    protected String refreshTokenEndpoint;
    protected String oauth2_scheme;

    public OAuth2Config(String username, String password, String clientId, String clientSecret, String accessTokenEndpoint, String refreshTokenEndpoint, String oauth2_scheme) {
        this.username = username;
        this.password = password;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.accessTokenEndpoint = accessTokenEndpoint;
        this.refreshTokenEndpoint = refreshTokenEndpoint;
        this.oauth2_scheme = oauth2_scheme;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getAccessTokenEndpoint() {
        return accessTokenEndpoint;
    }

    public String getRefreshTokenEndpoint() {
        return refreshTokenEndpoint;
    }

    public String getOAuth2Scheme() {
        return oauth2_scheme;
    }
    
    public static class Builder {

        private String oauth2_scheme = "Bearer";
        private String username;
        private String password;
        /*
         * OAuth2.0 client info
         */
        private String clientId;
        private String clientSecret;
        /*
         * OAuth2.0 remote endpoints
         */
        private String accessTokenEndpoint;
        private String refreshTokenEndpoint;

        public Builder setOAuth2Scheme(String oAuth2Scheme) {
            this.oauth2_scheme = oAuth2Scheme;
            return this;
        }

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setClientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public Builder setClientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
            return this;
        }

        public Builder setAccessTokenEndpoint(String accessTokenEndpoint) {
            this.accessTokenEndpoint = accessTokenEndpoint;
            return this;
        }

        public Builder setRefreshTokenEndpoint(String refreshTokenEndpoint) {
            this.refreshTokenEndpoint = refreshTokenEndpoint;
            return this;
        }

        public OAuth2Config build() {

            return new OAuth2Config(username, password, clientId, clientSecret, accessTokenEndpoint, refreshTokenEndpoint, oauth2_scheme);
        }
    }
}
