package org.resthub.core.domain.dao;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.core.domain.model.Resource;
import org.resthub.core.test.AbstractResourceDaoTest;


public class ResourceDaoTest extends AbstractResourceDaoTest<Resource> {

	@Inject
    @Named("resourceDao")
    @Override
    public void setResourceDao(ResourceDao<Resource> resourceDao) {
        super.setResourceDao(resourceDao);
    }

    

   
}
