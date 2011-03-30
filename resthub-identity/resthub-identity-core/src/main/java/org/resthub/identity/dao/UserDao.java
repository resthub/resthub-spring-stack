package org.resthub.identity.dao;

import java.util.List;
import org.resthub.identity.model.User;

/**
 *
 * @author "Nicolas Morel <nicolas.morel@atosorigin.com>"
 */
public interface UserDao extends PermissionsOwnerDao<User> {

    /**
     * Gets the user of a group.
     * @param groupName The name of the group.
     * @return A list of users corresponding to the given group.
     */
    List<User> getUsersFromGroup(String groupName);
}
