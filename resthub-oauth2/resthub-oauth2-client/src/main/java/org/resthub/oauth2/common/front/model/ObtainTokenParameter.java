package org.resthub.oauth2.common.front.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Parameter of the token obtention end-point, as described in the Oauth 2
 * specification (Section 4.1.2).
 */
@XmlRootElement
public class ObtainTokenParameter implements Serializable {

    private static final long serialVersionUID = 7530540767006126301L;

    // -----------------------------------------------------------------------------------------------------------------
    // Properties

    /**
     * Access grant type. <b>For now, only "password" supported</b>.
     */
    @XmlElement(name = "grant_type")
    @JsonProperty("grant_type")
    public String grant;

    /**
     * Client identifier. <b>Not used now, must be null</b>.
     */
    @XmlElement(name = "client_id")
    @JsonProperty("client_id")
    public String clientId;

    /**
     * Client secret. <b>Not used now, must be null</b>.
     */
    @XmlElement(name = "client_secret")
    @JsonProperty("client_secret")
    public String clientSecret;

    /**
     * Space separated list of object that will be accessed. <b>For now, must be
     * empty.</b>.
     */
    public String scope;

    /**
     * End-user name.
     */
    @XmlElement(name = "username")
    @JsonProperty("username")
    public String userName;

    /**
     * End-user password.
     */
    public String password;

    // -----------------------------------------------------------------------------------------------------------------
    // Constructors

    /**
     * Default constructor. Needed for JAX-B.
     */
    public ObtainTokenParameter() {

    } // Constructor.

    /**
     * Parametrized constructor.
     * 
     * @param clientId
     *            Client identifier. <b>Not used now, must be null</b>.
     * @param clientSecret
     *            Client secret. <b>Not used now, must be null</b>.
     * @param grant
     *            Access grant type. <b>For now, only "password" supported</b>.
     * @param scopes
     *            Space separated list of object that will be accessed. <b>For
     *            now, must be empty.</b>.
     * @param userName
     *            End-user name.
     * @param password
     *            End-user password.
     */
    public ObtainTokenParameter(String clientId, String clientSecret, String grant, String scope, String userName,
            String password) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.grant = grant;
        this.scope = scope;
        this.userName = userName;
        this.password = password;
    } // Constructor.

} // class ObtainTokenParameter
