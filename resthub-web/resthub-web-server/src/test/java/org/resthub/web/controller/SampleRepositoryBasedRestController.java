package org.resthub.web.controller;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.web.model.Sample;
import org.resthub.web.repository.WebSampleResourceRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller @RequestMapping("/repository-based")
public class SampleRepositoryBasedRestController extends
        RepositoryBasedRestController<Sample, Long, WebSampleResourceRepository> {

    @Inject
    @Named("webSampleResourceRepository")
    @Override
    public void setRepository(WebSampleResourceRepository repository) {
        this.repository = repository;
    }

    @Override
    public Long getIdFromResource(Sample resource) {
        return resource.getId();
    }

}
