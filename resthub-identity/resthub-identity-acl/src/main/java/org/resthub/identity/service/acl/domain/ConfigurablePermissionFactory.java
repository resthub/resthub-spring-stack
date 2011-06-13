package org.resthub.identity.service.acl.domain;

import java.util.List;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.security.acls.domain.DefaultPermissionFactory;
import org.springframework.security.acls.domain.PermissionFactory;
import org.springframework.security.acls.model.Permission;

/**
 * Default kazan implementation of {@link PermissionFactory}.
 * <p>
 * Used as a strategy by classes which wish to map integer masks and permission
 * names to <tt>Permission</tt> instances for use with the ACL implementation.
 * <p>
 * Maintains a registry of permission names and masks to <tt>Permission</tt>
 * instances.
 * 
 * @author Tantchonta M'PO
 */
public class ConfigurablePermissionFactory extends DefaultPermissionFactory {

    /**
     * List of classes containing Permissions.
     */
    protected List<String> permissionClasses;

    /**
     * Used by spring to inject classes that contains permissions.
     * 
     * @param permissionClasses
     *            array of class names (Permission subclasses).
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
                    Class<Permission> clazz = (Class<Permission>) getClass().getClassLoader().loadClass(className);
                    registerPublicPermissions(clazz);
                } catch (Exception exc) {
                    throw new BeanCreationException("Unknwon or invalid permission class '" + className + "'");
                }
            }
        } else {
            // Can't continue without any permissions
            throw new BeanCreationException("You must define classes containing permissions");
        }
    } // init().

} // class PermissionMapperService
