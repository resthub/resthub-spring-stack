package org.resthub.core.service;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.core.dao.SampleResourceDao;
import org.resthub.core.model.SampleResource;
import org.resthub.core.service.SampleResourceService;
import org.resthub.core.service.impl.GenericResourceServiceImpl;

@Named("sampleResourceService")
public class SampleResourceServiceImpl extends
		GenericResourceServiceImpl<SampleResource, SampleResourceDao> implements
		SampleResourceService {

	@Inject
	@Named("sampleResourceDao")
	@Override
	public void setDao(SampleResourceDao resourceDao) {
		// TODO Auto-generated method stub
		super.setDao(resourceDao);
	}

}
