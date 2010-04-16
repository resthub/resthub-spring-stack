package org.resthub.core.domain.dao;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Test;
import org.resthub.core.domain.model.Resource;
import org.resthub.core.test.AbstractResourceDaoTest;

public class ResourceDaoTest extends AbstractResourceDaoTest<Resource, ResourceDao> {

    @Inject
    @Named("resourceDao")
    @Override
	public void setResourceDao(ResourceDao resourceDao) {
		// TODO Auto-generated method stub
		super.setResourceDao(resourceDao);
	}

	@Override
    @Test(expected = UnsupportedOperationException.class)
    public void testUpdate() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
