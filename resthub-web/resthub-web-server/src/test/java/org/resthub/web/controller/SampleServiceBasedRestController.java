package org.resthub.web.controller;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.web.model.Sample;
import org.resthub.web.service.WebSampleResourceService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller @RequestMapping("/service-based")
public class SampleServiceBasedRestController extends
        ServiceBasedRestController<Sample, Long, WebSampleResourceService> {

    @Inject
    @Named("webSampleResourceService")
    @Override
    public void setService(WebSampleResourceService service) {
        this.service = service;
    }

    @Override
    public Long getIdFromResource(Sample webSampleResource) {
        return webSampleResource.getId();
    }
}
