package org.resthub.oauth2.httpclient;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Response of the token end-point, as described in the Oauth 2 specification
 * (Section 4.2).
 */
@XmlRootElement
@XmlType(propOrder = { "accessToken", "expiresIn", "refreshToken", "scope" })
public class OAuth2TokenResponse implements Serializable {

    private static final long serialVersionUID = 3734476795524671722L;

    // -----------------------------------------------------------------------------------------------------------------
    // Properties

    /**
     * The access token issued by the authorization server.
     */
    @XmlElement(name = "access_token")
    @JsonProperty("access_token")
    public String accessToken;

    /**
     * The duration in seconds of the access token lifetime.
     */
    @XmlElement(name = "expires_in")
    @JsonProperty("expires_in")
    public Integer expiresIn;

    /**
     * The refresh token used to obtain new access tokens using the same
     * end-user access grant.
     */
    @XmlElement(name = "refresh_token")
    @JsonProperty("refresh_token")
    public String refreshToken;

    /**
     * The scope of the access token as a list of space-delimited strings. The
     * value of the "scope" parameter is defined by the authorization server. If
     * the value contains multiple space-delimited strings, their order does not
     * matter, and each string adds an additional access range to the requested
     * scope.
     */
    @XmlElement(name = "scope")
    @JsonProperty("scope")
    public String scope;

    /**
     * Default constructor. Needed for JAX-B.
     */
    public OAuth2TokenResponse() {

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
