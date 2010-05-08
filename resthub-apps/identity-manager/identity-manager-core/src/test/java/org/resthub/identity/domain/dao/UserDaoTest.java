package org.resthub.identity.domain.dao;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Test;
import org.resthub.core.test.AbstractResourceDaoTest;
import org.resthub.identity.dao.UserDao;
import org.resthub.identity.model.User;

/**
 *
 * @author Loïc Frering <loic.frering@atosorigin.com>
 */
public class UserDaoTest extends AbstractResourceDaoTest<User, UserDao> {

	@Inject
    @Named("userDao")
    @Override
    public void setResourceDao(UserDao resourceDao) {
        super.setResourceDao(resourceDao);
    }
	
	@Override
    @Test(expected = UnsupportedOperationException.class)
    public void testUpdate() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
