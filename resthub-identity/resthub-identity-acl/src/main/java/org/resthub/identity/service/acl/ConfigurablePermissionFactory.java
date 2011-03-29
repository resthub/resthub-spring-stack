package org.resthub.identity.service.acl;

import java.util.List;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.security.acls.domain.DefaultPermissionFactory;
import org.springframework.security.acls.model.Permission;

/**
 * TODO
 */
public class ConfigurablePermissionFactory extends DefaultPermissionFactory {
	
	/**
	 * List of classes containing Permissions.
	 */
	protected List<String> permissionClasses;
	
	/**
	 * Used by spring to inject classes that contains permissions.
	 * 
	 * @param permissionClasses array of class names (Permission subclasses).
	 */
	public void setPermissionClasses(List<String> permissionClasses) {
		this.permissionClasses = permissionClasses;
	} // setPermissionClasses().

	/**
	 * Loads the permissions from their classes.
	 */
	public void init() {
		if (permissionClasses != null || permissionClasses.size() == 0) {
			for (String className : permissionClasses) {
				// Gets the class and loads its permissions.
				try {
					@SuppressWarnings("unchecked")
					Class<Permission> clazz = (Class<Permission>)getClass().getClassLoader().loadClass(className);
					registerPublicPermissions(clazz);
				} catch (Exception exc) {
					throw new BeanCreationException("Unknwon or invalid permission class '"+ className +"'");
				}
			}
		} else {
			// Can't continue without any permissions 
			throw new BeanCreationException("You must define classes containing permissions");
		}
	} // init().
	
} // class PermissionMapperService
