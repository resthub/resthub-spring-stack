package org.resthub.web.service.impl;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.common.service.RepositoryBasedRestService;
import org.resthub.web.model.WebSampleResource;
import org.resthub.web.repository.WebSampleResourceRepository;
import org.resthub.web.service.WebSampleResourceService;

@Named("webSampleResourceService")
public class WebSampleResourceServiceImpl extends RepositoryBasedRestService<WebSampleResource, Long, WebSampleResourceRepository>
        implements WebSampleResourceService {

    @Inject
    @Named("webSampleResourceRepository")
    @Override
    public void setRepository(WebSampleResourceRepository webSampleResourceRepository) {
        super.setRepository(webSampleResourceRepository);
    }
}
