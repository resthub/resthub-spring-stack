package org.resthub.core.service;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.core.dao.SampleResourceDao;
import org.resthub.core.model.SampleResource;

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
