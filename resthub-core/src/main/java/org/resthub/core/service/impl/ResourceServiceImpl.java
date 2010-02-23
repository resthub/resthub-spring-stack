package org.resthub.core.service.impl;

import javax.inject.Named;

import org.resthub.core.domain.dao.ResourceDao;
import org.resthub.core.domain.model.Resource;

@Named("resourceService")
public class ResourceServiceImpl extends AbstractResourceServiceImpl<Resource, ResourceDao<Resource>> {

}
