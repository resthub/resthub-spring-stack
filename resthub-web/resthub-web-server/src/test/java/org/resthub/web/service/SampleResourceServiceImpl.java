package org.resthub.web.service;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.common.service.CrudServiceImpl;
import org.resthub.web.model.Sample;
import org.resthub.web.repository.SampleResourceRepository;

@Named("webSampleResourceService")
public class SampleResourceServiceImpl extends CrudServiceImpl<Sample, Long, SampleResourceRepository> implements
        SampleResourceService {

    @Override
    @Inject
    public void setRepository(SampleResourceRepository repository) {
        super.setRepository(repository);
    }
    
    @Override
    public Sample findByName(String name) {
        return repository.findByName(name);
    }
}
