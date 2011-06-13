package org.resthub.identity.dao;

import java.util.List;

import org.resthub.core.dao.GenericDao;
import org.resthub.identity.model.AbstractPermissionsOwner;
import org.resthub.identity.model.Group;

/**
 * Dao for generic operations on AbstractPermissionsOwner entities.
 * 
 * @author "Nicolas Morel <nicolas.morel@atosorigin.com>"
 */
public interface AbstractPermissionsOwnerDao extends GenericDao<AbstractPermissionsOwner, Long> {

    /**
     * Gets all the AbstractPermissionsOwners that have a role.
     * 
     * @param roles
     *            A list of roles to look for.
     * @return A list of AbstractPermissionsOwners having at least one of the
     *         roles defined as parameter.
     */
    List<AbstractPermissionsOwner> getWithRoles(List<String> roles);

    /**
     * Gets all the AbstractPermissionsOwners that have the specified group as
     * parent.
     * 
     * @param group
     *            The parent group.
     * @return The list of AbstractPermissionsOwners that are associated with
     *         the group.
     */
    List<AbstractPermissionsOwner> getWithGroupAsParent(Group group);
}
