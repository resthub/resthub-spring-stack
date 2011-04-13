package org.resthub.identity.service;

import org.resthub.core.service.GenericResourceService;
import org.resthub.identity.model.Role;

/**
 *
 * @author "Nicolas Morel <nicolas.morel@atosorigin.com>"
 */
public interface RoleService extends GenericResourceService<Role> {

    /**
     * Find the role with its name.
     * @param name Name to search.
     * @return The corresponding role.
     */
    Role findByName(String name);
}
