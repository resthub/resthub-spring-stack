package org.resthub.identity.dao;

import javax.inject.Named;
import org.resthub.core.dao.GenericJpaResourceDao;
import org.resthub.identity.model.Role;

/**
 *
 * @author "Nicolas Morel <nicolas.morel@atosorigin.com>"
 */
@Named("roleDao")
public class RoleDaoImpl extends GenericJpaResourceDao<Role> implements RoleDao {

}
