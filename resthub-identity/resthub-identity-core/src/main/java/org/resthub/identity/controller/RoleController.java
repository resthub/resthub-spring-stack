package org.resthub.identity.controller;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Path;
import org.resthub.identity.model.Role;
import org.resthub.identity.service.RoleService;
import org.resthub.web.controller.GenericResourceController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author "Nicolas Morel <nicolas.morel@atosorigin.com>"
 */
@Path("/role")
@RolesAllowed({"IM-ADMIN"})
@Named("roleController")
public class RoleController extends GenericResourceController<Role, RoleService> {

    /**
     * Provide a logger for the whole class.
     */
    private final Logger logger = LoggerFactory.getLogger(GroupController.class);

    @Inject
    @Named("roleService")
    @Override
    /**
     * {@inheritDoc}
     */
    public void setService(RoleService service) {
        this.service = service;
    }
}
