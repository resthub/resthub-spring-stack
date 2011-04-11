package org.resthub.identity.service;

import javax.inject.Inject;
import javax.inject.Named;
import org.resthub.core.service.GenericResourceServiceImpl;
import org.resthub.identity.dao.RoleDao;
import org.resthub.identity.model.Role;

/**
 * 
 * @author "Nicolas Morel <nicolas.morel@atosorigin.com>"
 */
@Named("roleService")
public class RoleServiceImpl extends GenericResourceServiceImpl<Role, RoleDao> implements RoleService {
    /**
     * ${@inheritDoc}
     */
    @Override
    @Inject
    @Named("roleDao")
    public void setDao(RoleDao resourceDao) {
        super.setDao(resourceDao);
    }

}
