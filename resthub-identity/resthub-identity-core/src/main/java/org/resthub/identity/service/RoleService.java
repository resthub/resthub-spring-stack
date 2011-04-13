package org.resthub.identity.service;

import org.resthub.core.service.GenericResourceService;
import org.resthub.identity.model.Role;
import org.resthub.identity.service.tracability.TracableService;

/**
 *
 * @author "Nicolas Morel <nicolas.morel@atosorigin.com>"
 */
public interface RoleService extends GenericResourceService<Role>, TracableService {

    /**
     * Kind of changes notified for role events
     */
    enum RoleChange {

        /**
         * Role creation. Arguments :
         * 1- created role.
         */
        ROLE_CREATION,
        /**
         * Role deletion. Arguments :
         * 1- deleted role.
         */
        ROLE_DELETION,
        /**
         * Role addition to a Group. Arguments :
         * 1- added role.
         * 2- concerned parent group.
         */
        ROLE_ADDED_TO_GROUP,
        /**
         * Role removal from a Group. Arguments :
         * 1- removed role.
         * 2- concerned parent group.
         */
        ROLE_REMOVED_FROM_GROUP,
        /**
         * Role addition to a user. Arguments :
         * 1- added role.
         * 2- concerned parent user.
         */
        ROLE_ADDED_TO_USER,
        /**
         * Role removal from a user. Arguments :
         * 1- removed role.
         * 2- concerned parent user.
         */
        ROLE_REMOVED_FROM_USER
    };

    /**
     * Find the role with its name.
     * @param name Name to search.
     * @return The corresponding role.
     */
    Role findByName(String name);
}
