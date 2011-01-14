package org.resthub.oauth2.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * Stores in database the user token, and its creation date.
 */
@Entity
@Table(name="tokens")
@Access(AccessType.FIELD)
@XmlRootElement
public class Token implements Serializable {

	private static final long serialVersionUID = 2902107409296353744L;

	// -----------------------------------------------------------------------------------------------------------------
	// Properties
	
	/**
	 * Unic identifier in db
	 */
	@Id
	@GeneratedValue
	public Long id;
	
	/**
	 * The token's value.
	 */
	@Column(unique = true)
	public String accessToken = null;
	
	/**
	 * The corresponding user's identifier.
	 */
	public String userId;
	
	/**
	 * The token's creation date.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	public Date createdOn = new Date();
	
	/**
	 * The token life time, in seconds.
	 * 15 minutes by default
	 */
	public Integer lifeTime = 900;
	
	/**
	 * Corresponding refresh token.
	 */
	public String refreshToken = null;
	
	/**
	 * Corresponding access code.
	 */
	public String accessCode = null;
	
	/**
	 * The user's permissions.
	 */
	@ElementCollection(fetch=FetchType.EAGER)
	@CollectionTable(name = "user_permissions")  
	public List<String> permissions = new ArrayList<String>();
	
	// -----------------------------------------------------------------------------------------------------------------
	// Inherited Object methods
	
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        boolean isEqual = false;
        if (obj instanceof Token) {
        	final Token other = (Token)obj;
        	isEqual = id == null ? other.id == null : id.equals(other.id);
        }
        return isEqual;
    } // equals().

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return getClass().getName().hashCode()+(this.id != null ? this.id.hashCode() : 0);
    } // hashCode().
    
} // classe Token
 