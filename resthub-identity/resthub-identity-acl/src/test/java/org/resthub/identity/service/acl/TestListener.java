package org.resthub.identity.service.acl;

import org.resthub.identity.service.tracability.ServiceListener;

/**
 * Simple listener for test purposes
 */
public class TestListener implements ServiceListener {
	
	/**
	 * Last notified type.
	 */
	public String lastType = null;
	
	/**
	 * Last notified arguments.
	 */
	public Object[] lastArguments = null;
	
	/**
	 * {@inheritDoc}
	 */
	public void onChange(String type, Object ... arguments) {
		clear();
		this.lastType = type;
		this.lastArguments = arguments;
	} // onChange().
	
	/**
	 * Reset all fields.
	 */
	public void clear() {
		this.lastArguments = null;
		this.lastType = null;
	} // clear().
	
} // Class TestListerner
