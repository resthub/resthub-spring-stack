package org.resthub.identity.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

/**
 * Describe a user account.<br/>
 * A User has some attributes such as login, password, email, ... TODO there is
 * * some fields which have in comments "Nullable=false" , this cannot be remove
 * from comments without refactoring the abstractTestDao class
 */
@Entity
@Table(name="idm_users")
@XmlRootElement
@Indexed
public class User extends AbstractPermissionsOwner {

	private static final long serialVersionUID = -7139715798005612136L;
	/**
	 * List of attributes for a user
	 * */
	protected String firstName = null;
	protected String lastName = null;
	protected String login = null;
	protected String password = null;
	protected String email = null;


	/**
	 * default Constructor
	 * */
	public User() {
	}

	public User(User u){
		super(u);
		String s;
		s=u.getFirstName();
		firstName= (s==null) ? new String() : new String(s);
		s=u.getLastName();
		lastName = (s==null) ? new String() : new String(s);
		s=u.getLogin();
		login = (s==null) ? new String() : new String(s);
		s=u.getEmail();
		email = (s==null) ? new String() : new String(s);
		List<Group> l = u.getGroups();
		groups= (l==null) ? new ArrayList<Group>() : new ArrayList<Group>(u.getGroups());
	}
	/**
	 * getLogin
	 * 
	 * @return the user login
	 * */
	@Field
	@Column(unique = true , nullable = false )
	public String getLogin() {
		return login;
	}

	/**
	 * setLogin
	 * 
	 * @param login
	 *            the login to be set for the user
	 * */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * gets the Password<br/>
	 * The password can not be given in the XML/JSON representation of the user
	 * 
	 * @return user's password
	 * */
	@Column( nullable = false )
	@XmlTransient
	@JsonIgnore
	public String getPassword() {
		return password;
	}

	/**
	 * sets the user's Password
	 * 
	 * @param password
	 *            ,the password to be set to the user
	 * */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * gets the user's FirstName
	 * 
	 * @return user's FirstName
	 * */
	@Field
	@Column
	public String getFirstName() {
		return firstName;
	}

	/**
	 * sets the user's FirstName
	 * 
	 * @param firstName
	 *            the firstName to be set
	 * */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * get's the user's lastName
	 * 
	 * @return user's lastName
	 * */
	@Field
	@Column
	public String getLastName() {
		return lastName;
	}

	/**
	 * sets the user's LastName
	 * 
	 * @param lastName
	 *            the lastName to be set
	 * */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * gets the user's Email
	 * 
	 *@return user's email;
	 * */
	@Field
	@Column(/* nullable = false */)
	public String getEmail() {
		return email;
	}

	/**
	 * sets the user's email
	 * 
	 * @param email
	 *            the email to be set
	 * */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * returns a {@link String} representation of the user. Display the ID,
	 * Login and email of the user
	 * */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("User[");
		sb.append("Id: ").append(this.getId()).append(", ");
		sb.append("Login: ").append(this.getLogin()).append(", ");
		sb.append("Email: ").append(this.getEmail());
		sb.append("]");
		return sb.toString();
	}
	
	public String generateDefaultPassword(){
		String s="P455W0R[)";
		return s;
	}
	
}
