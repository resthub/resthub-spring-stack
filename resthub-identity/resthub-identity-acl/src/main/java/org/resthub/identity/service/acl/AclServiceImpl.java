package org.resthub.identity.service.acl;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import org.resthub.identity.service.acl.domain.ConfigurablePermissionFactory;
import org.resthub.identity.service.tracability.ServiceListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the ACL Service based on Spring Security. Provides support
 * for creating and storing <code>Acl</code> instances using a specific resource
 * identifier
 * 
 * 
 * @author Sebastien DELEUZE
 * @author Damien FEUGAS
 * @author Tantchonta M'PO
 * 
 */
@Named("idmAclService")
public class AclServiceImpl implements AclService {

    private static final Logger logger = LoggerFactory.getLogger(AclServiceImpl.class);

    // -----------------------------------------------------------------------------------------------------------------
    // Protected attributes

    /**
     * Spring Security's ACL facility. Injected by Spring.
     */
    @Inject
    @Named("aclService")
    protected JdbcMutableAclService aclService;

    /**
     * Datasource used to access to database.
     */
    @Inject
    @Named("dataSource")
    protected DataSource datasource;

    /**
     * Mapper between strings and permissions. Injected by Spring.
     */
    @Inject
    protected ConfigurablePermissionFactory permissionFactory;

    /**
     * Set of registered listeners
     */
    protected Set<ServiceListener> listeners = new HashSet<ServiceListener>();

    // -----------------------------------------------------------------------------------------------------------------
    // Protected methods

    /**
     * Sends a notification to every listernes registered. Do not fail if a user
     * thrown an exception (report exception in logs).
     * 
     * @param type
     *            Type of notification.
     * @param arguments
     *            Notification arguments.
     */
    protected void publishChange(String type, Object... arguments) {
        for (ServiceListener listener : listeners) {
            try {
                // Sends notification to each known listeners
                listener.onChange(type, arguments);
            } catch (Exception exc) {
                // Log exception
                logger.warn("[publishChange] Cannot bublish " + type + " changes", exc);
            }
        }
    } // publishChange().

    // -----------------------------------------------------------------------------------------------------------------
    // public methods

    /**
     * Initialization: creates Spring Security's tables, using datasourcE.
     */
    @PostConstruct
    public void init() {
        try {
            Connection connection = datasource.getConnection();
            DatabaseMetaData metatData = connection.getMetaData();
            String productName = metatData.getDatabaseProductName().toLowerCase();

            if (productName.equals("postgresql")) {
                logger.info("PostgreSQL support activated for ACL Service");
                aclService.setClassIdentityQuery("select currval(pg_get_serial_sequence('acl_class', 'id'))");
                aclService.setSidIdentityQuery("select currval(pg_get_serial_sequence('acl_sid', 'id'))");
            }

        } catch (Exception exc) {
            throw new BeanCreationException("Cannot set SpringSecurity ACL tables in db", exc);
        }

    } // init().

    // -----------------------------------------------------------------------------------------------------------------
    // Inherited methods

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveAcl(Object domainObject, Serializable domainObjectId, String ownerId, String permission) {
        // Sid identifies the user.
        Sid owner = new PrincipalSid(ownerId);
        // ObjectIdentity is a unic identifier for the model object
        ObjectIdentity oid = new ObjectIdentityImpl(domainObject.getClass(), domainObjectId);

        // Creates the acl, or update the existing one.
        MutableAcl acl;
        try {
            acl = (MutableAcl) aclService.readAclById(oid);
        } catch (NotFoundException nfe) {
            acl = aclService.createAcl(oid);
        }

        // Update the acl for this user on this model object.
        acl.insertAce(acl.getEntries().size(), permissionFactory.buildFromName(permission), owner, true);

        aclService.updateAcl(acl);
        // Publish ACL creation.
        publishChange(AclServiceChange.ACL_CREATION.name(), domainObjectId, ownerId, permission);
    } // saveAcl().

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeAcl(Object domainObject, Serializable domainObjectId, String ownerId, String permission) {
        // Sid identifies the user.
        Sid owner = new PrincipalSid(ownerId);
        // ObjectIdentity is a unic identifier for the model object
        ObjectIdentity oid = new ObjectIdentityImpl(domainObject.getClass(), domainObjectId);

        // Gets the existing acl.
        MutableAcl acl = (MutableAcl) aclService.readAclById(oid);
        Permission effectivePerm = permissionFactory.buildFromName(permission);

        // Remove all permissions associated with this particular recipient
        // (string equality to KISS)
        List<AccessControlEntry> entries = acl.getEntries();
        for (int i = entries.size() -1 ; i >= 0 ; i--) {
            if (entries.get(i).getSid().equals(owner) && entries.get(i).getPermission().equals(effectivePerm)) {
                acl.deleteAce(i);
            }
        }

        // Update the acl for this user on this model object.
        aclService.updateAcl(acl);
        // Publish ACL creation.
        publishChange(AclServiceChange.ACL_DELETION.name(), domainObjectId, ownerId, permission);
    } // removeAcl().

    /**
     * {@inheritDoc}
     */
    @Override
    public Acl getAcls(Object domainObject, Serializable domainObjectId) {
        // ObjectIdentity is a unic identifier for the model object
        ObjectIdentity oid = new ObjectIdentityImpl(domainObject.getClass(), domainObjectId);
        MutableAcl acl = (MutableAcl) aclService.readAclById(oid);
        return acl;
    } // getAcl().

    /**
     * {@inheritDoc}
     */
    @Override
    public void addListener(ServiceListener listener) {
        // Adds a new listener if needed.
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    } // addListener().

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeListener(ServiceListener listener) {
        // Adds a new listener if needed.
        if (listeners.contains(listener)) {
            listeners.remove(listener);
        }
    } // removeListener().

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveAcls(Object domainObject, Serializable domainObjectId, String userId, List<String> permissions) {
        for (String permission : permissions) {
            this.saveAcl(domainObject, domainObjectId, userId, permission);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeAcls(Object domainObject, Serializable domainObjectId, String userId, List<String> permissions) {
        for (String permission : permissions) {
            this.removeAcl(domainObject, domainObjectId, userId, permission);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false)
    public void addPermission(Object domainObject, Sid recipient, String permissionName) {
        addPermission(domainObject, recipient, permissionFactory.buildFromName(permissionName));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false)
    public void deletePermission(Object domainObject, Sid recipient, String permissionName) {
        deletePermission(domainObject, recipient, permissionFactory.buildFromName(permissionName));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false)
    public void addPermission(Object domainObject, Sid recipient, int mask) {
        addPermission(domainObject, recipient, permissionFactory.buildFromMask(mask));
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = false)
    public void addPermission(Object domainObject, Sid recipient, Permission permission) {
        MutableAcl acl;
        ObjectIdentity oid = new ObjectIdentityImpl(domainObject);

        try {
            acl = (MutableAcl) aclService.readAclById(oid);

        } catch (NotFoundException nfe) {
            acl = aclService.createAcl(oid);

        }

        acl.insertAce(acl.getEntries().size(), permission, recipient, true);
        aclService.updateAcl(acl);

        logger.debug("Added permission " + permission.toString() + " for Sid " + recipient.toString() + " to object "
                + domainObject);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = false)
    public void deletePermission(Object domainObject, Sid recipient, Permission permission) {
        ObjectIdentity oid = new ObjectIdentityImpl(domainObject);
        MutableAcl acl = (MutableAcl) aclService.readAclById(oid);

        List<AccessControlEntry> entries = acl.getEntries();
        if (entries.size() > 1) {
        	for (int i = entries.size() -1 ; i >= 0 ; i--) {
                if (entries.get(i).getSid().equals(recipient) && entries.get(i).getPermission().equals(permission)) {
                    acl.deleteAce(i);
                }
            }
        }

        aclService.updateAcl(acl);
        logger.debug("Deleted resource " + domainObject + " ACL permissions for recipient " + recipient);

    }

} // class AclServiceImpl.
