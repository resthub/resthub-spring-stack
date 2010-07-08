package org.resthub.oauth2.filter.front;

import java.security.Principal;
import java.util.ArrayList;
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
	
} // class SecuredHttpRequest.