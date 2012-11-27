package org.resthub.web.service.impl;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.common.service.CrudServiceImpl;
import org.resthub.web.model.Sample;
import org.resthub.web.repository.SampleResourceRepository;
import org.resthub.web.service.SampleResourceService;
import org.springframework.context.annotation.Profile;

@Named("webSampleResourceService")
public class SampleResourceServiceImpl extends CrudServiceImpl<Sample, Long, SampleResourceRepository> implements
        SampleResourceService {

    @Override
    @Inject
    public void setRepository(SampleResourceRepository webSampleResourceRepository) {
        super.setRepository(webSampleResourceRepository);
    }
    
    @Override
    public Sample findByName(String name) {
        return repository.findByName(name);
    }
}
