package org.resthub.core.service.impl;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.core.domain.dao.SampleResourceDao;
import org.resthub.core.domain.model.SampleResource;
import org.resthub.core.service.SampleResourceService;

@Named("sampleResourceService")
public class SampleResourceServiceImpl extends
		GenericResourceServiceImpl<SampleResource, SampleResourceDao> implements
		SampleResourceService {

	@Inject
	@Named("sampleResourceDao")
	@Override
	public void setResourceDao(SampleResourceDao resourceDao) {
		// TODO Auto-generated method stub
		super.setResourceDao(resourceDao);
	}

}
