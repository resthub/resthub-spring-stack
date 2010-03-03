package org.resthub.core.domain.dao;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Test;
import org.resthub.core.domain.model.Resource;
import org.resthub.core.test.AbstractResourceDaoTest;

public class ResourceDaoTest extends AbstractResourceDaoTest<Resource> {

    @Inject
    @Named("resourceDao")
    @Override
    public void setResourceDao(ResourceDao<Resource> resourceDao) {
        super.setResourceDao(resourceDao);
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void testUpdate() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
