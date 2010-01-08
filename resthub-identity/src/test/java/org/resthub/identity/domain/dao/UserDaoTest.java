package org.resthub.identity.domain.dao;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.core.domain.dao.ResourceDao;
import org.resthub.core.domain.model.User;
import org.resthub.core.test.AbstractResourceDaoTest;

/**
 *
 * @author Loïc Frering <loic.frering@atosorigin.com>
 */
public class UserDaoTest extends AbstractResourceDaoTest<User> {

	@Inject
    @Named("userDao")
    @Override
    public void setResourceDao(ResourceDao<User> resourceDao) {
        super.setResourceDao(resourceDao);
    }
}
