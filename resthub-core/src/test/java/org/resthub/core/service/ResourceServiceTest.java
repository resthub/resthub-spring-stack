package org.resthub.core.service;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.core.domain.model.Resource;
import org.resthub.core.test.AbstractResourceServiceTest;

public class ResourceServiceTest extends AbstractResourceServiceTest<Resource> {
    
	@Inject
    @Named("resourceService")
    @Override
    public void setResourceService(ResourceService<Resource> resourceService) {
        super.setResourceService(resourceService);
    }
	
}
