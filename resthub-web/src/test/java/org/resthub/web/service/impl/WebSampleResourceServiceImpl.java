package org.resthub.web.service.impl;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.core.service.impl.GenericResourceServiceImpl;
import org.resthub.web.domain.dao.WebSampleResourceDao;
import org.resthub.web.domain.model.WebSampleResource;
import org.resthub.web.service.WebSampleResourceService;

@Named("webSampleResourceService")
public class WebSampleResourceServiceImpl extends
		GenericResourceServiceImpl<WebSampleResource, WebSampleResourceDao> implements
		WebSampleResourceService {

	@Inject
	@Named("webSampleResourceDao")
	@Override
	public void setResourceDao(WebSampleResourceDao resourceDao) {
		// TODO Auto-generated method stub
		super.setResourceDao(resourceDao);
	}

}
