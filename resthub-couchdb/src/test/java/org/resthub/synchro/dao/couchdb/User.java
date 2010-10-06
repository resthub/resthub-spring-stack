package org.resthub.synchro.dao.couchdb;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

/**
 * Test POJO used for SynchroDao unit tests.
 */
@JsonSerialize(include = Inclusion.NON_NULL)
public class User {

	/**
	 * Primary key : id. Set by CouchDB
	 */
	@JsonProperty("_id")
	private String id;

	/**
	 * Revision. Internaly used by CoucchDB.
	 */
	@JsonProperty("_rev")
	@SuppressWarnings("unused")
	private String rev;

	/**
	 * User's primary key.
	 * @return The primary key.
	 */
	@JsonIgnore
	public String getId() {
		return id;
	}
	
	/**
	 * User's first name
	 */
	public String firstName;
	
	/**
	 * User's last name.
	 */
	public String lastName;
	
	/**
	 * Indicates if an object is logically equals to the current object.
	 * 
	 * @param obj The compared object.
	 * @return true if the compared object is a User and has the same id than the current object, false otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		boolean isEqual = false;
		if (obj instanceof User) {
			User other = (User)obj;
			isEqual = other.id == null ? id == null : other.id.equals(id);
		}
		return isEqual;
	} // equals().
	
} // Class User
