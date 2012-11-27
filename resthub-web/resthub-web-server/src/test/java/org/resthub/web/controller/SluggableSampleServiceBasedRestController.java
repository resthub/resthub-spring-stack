package org.resthub.web.controller;

import javax.inject.Inject;
import org.resthub.common.exception.NotFoundException;

import org.resthub.web.model.Sample;
import org.resthub.web.service.SampleResourceService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sluggable-service-based")
@Profile("resthub-jpa")
public class SluggableSampleServiceBasedRestController extends
        ServiceBasedRestController<Sample, String, SampleResourceService> {

    @Override
    @Inject
    public void setService(SampleResourceService service) {
        this.service = service;
    }

    @Override
    public Sample findById(@PathVariable String id) {
        Sample sample = this.service.findByName(id);
        if (sample == null) {
            throw new NotFoundException();
        }
        return sample;
    }
    
}
