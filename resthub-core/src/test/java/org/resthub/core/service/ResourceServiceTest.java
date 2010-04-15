package org.resthub.core.service;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Test;
import org.resthub.core.domain.model.Resource;
import org.resthub.core.test.AbstractResourceServiceTest;

public class ResourceServiceTest extends AbstractResourceServiceTest<Resource, ResourceService> {

	@Inject
    @Named("resourceService")
    @Override
	public void setResourceService(
			ResourceService resourceService) {
		// TODO Auto-generated method stub
		super.setResourceService(resourceService);
	}

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void testUpdate() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
