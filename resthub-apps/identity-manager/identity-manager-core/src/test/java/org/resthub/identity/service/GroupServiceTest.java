package org.resthub.identity.service;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Test;
import org.resthub.core.service.GenericResourceService;
import org.resthub.core.test.AbstractResourceServiceTest;
import org.resthub.identity.model.Group;

public class GroupServiceTest extends AbstractResourceServiceTest<Group, GenericResourceService<Group>> {

	@Inject
	@Named("groupService")
	@Override
	public void setResourceService(GenericResourceService<Group> resourceService) {
		super.setResourceService(resourceService);
	}

	@Override
    @Test(expected = UnsupportedOperationException.class)
    public void testUpdate() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
