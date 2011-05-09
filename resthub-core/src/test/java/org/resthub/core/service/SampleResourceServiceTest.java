package org.resthub.core.service;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Test;
import org.resthub.core.model.SampleResource;
import org.resthub.core.test.service.AbstractResourceServiceTest;

public class SampleResourceServiceTest extends
		AbstractResourceServiceTest<SampleResource, SampleResourceService> {

	@Inject
	@Named("sampleResourceService")
	public void setResourceService(SampleResourceService resourceService) {
		super.setResourceService(resourceService);
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testUpdate() throws Exception {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
