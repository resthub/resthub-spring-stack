package org.resthub.oauth2.filter.front;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * This wrapper allows the OAuth 2 filter to enrich the request with the user informations.<br/><br/>
 * 
 * The following token informations are available :
 * <ul>
 * <li>userId - set as "Principal"</li>
 * <li>permissions - set as "roles"</li>
 * </ul>
 */
public class SecuredHttpRequest extends HttpServletRequestWrapper {

	// -----------------------------------------------------------------------------------------------------------------
	// Public constants

	/**
	 * Fake header name that contains the connected user_id.
	 */
	public static final String USER_ID = "user_id";
	
	// -----------------------------------------------------------------------------------------------------------------
	// Protected attributes

	/**
	 * The user id.
	 */
	protected String userId;
	
	/**
	 * The user permissions
	 */
	protected List<String> permissions = new ArrayList<String>();
	
	/**
	 * The inner HTTP request.
	 */
	protected HttpServletRequest innerRequest;

	// -----------------------------------------------------------------------------------------------------------------
	// Constructor

	/**
	 * Constructor a wrapper.
	 * 
	 * @param userId The user's identifier.
	 * @param permissions The user's permissions
	 * @param request The wrapped request.
	 */
	public SecuredHttpRequest(String userId, List<String> permissions, HttpServletRequest request) {
		super(request);
		this.userId = userId;
		this.permissions = permissions;
		this.innerRequest = request;
	} // constructor.

	// -----------------------------------------------------------------------------------------------------------------
	// Public HttpServletRequestWrapper inherited methods

	/**
	 * Indicates if the authenticated user has or not a given permission.
	 * 
	 * @param permission The checked permission.
	 * @return true if the authenticated user has this permission.
	 */
	@Override
	public boolean isUserInRole(String permission) {
		boolean found = false;
		if (permissions == null) {
			found = this.innerRequest.isUserInRole(permission);
		} else {
			found = permissions.contains(permission);
		}
		return found;
	} // isUserInRole().
	
	/**
	 * The wrapped request is always secured.
	 * 
	 * @return true.
	 */
	@Override
    public boolean isSecure() {
        return true;
    } // isSecured().

	/**
	 * Return the user'id, if set.
	 */
	@Override
	public Principal getUserPrincipal() {
		Principal principal = null;
		if (this.userId == null) {
			principal = innerRequest.getUserPrincipal();
		} else {
			// Makes an anonymous implementation to just return our user
			principal = new Principal() {
					@Override
					public String getName() {
						return userId;
					}
				};
		}
		return principal;
	} // getUserPrincipal().
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getHeader(String name) {
		// Superclass treatment. 
		String header = super.getHeader(name);
		// If someone asks for USER_ID header, sends it.
		if (USER_ID.equals(name)) {
			header = userId;
		}
		return header;
	} // getHeader()
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Enumeration<String> getHeaders(String name) {
		// Superclass treatment. 
		Enumeration<String> headers = super.getHeaders(name);
		// If someone asks for USER_ID header, sends it.
		if (USER_ID.equals(name)) {
			List<String> values= new ArrayList<String>();
			values.add(userId);
			headers = Collections.enumeration(values);
		}
		return headers;
	} // getHeaders()

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Enumeration<?> getHeaderNames() {
		// Superclass treatment. 
		Enumeration<String> headerNames = super.getHeaderNames();
		ArrayList<String> list = new ArrayList<String>();
		while(headerNames.hasMoreElements()) {
			// add the names of the request headers into the list
			String n = (String)headerNames.nextElement();
			list.add(n);
		}
		// Adds the USER_ID fake header.
		if (!list.contains(USER_ID)) {
			list.add(USER_ID);
		}
		// Returns an enumeration from the list
		return Collections.enumeration(list);
	} // getHeaderNames()

} // class SecuredHttpRequest.