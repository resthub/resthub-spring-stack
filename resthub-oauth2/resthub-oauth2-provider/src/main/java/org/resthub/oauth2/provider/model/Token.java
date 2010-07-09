package org.resthub.oauth2.provider.model;

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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import org.resthub.core.model.Resource;

/**
 * Stores in database the user token, and its creation date.
 */
@Entity
@Table(name="tokens")
@Access(AccessType.FIELD)
@XmlRootElement
public class Token extends Resource implements Serializable {

	private static final long serialVersionUID = 2902107409296353744L;

	// -----------------------------------------------------------------------------------------------------------------
	// Properties
	
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
	 * The user's permissions.
	 */
	@ElementCollection  
	@CollectionTable(name = "user_permissions")  
	public List<String> permissions = new ArrayList<String>();
	
} // classe Token
 