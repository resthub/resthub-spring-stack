package org.resthub.web.controller;

import javax.inject.Inject;

import org.resthub.web.model.Sample;
import org.resthub.web.service.SampleResourceService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/service-based")
public class SampleServiceBasedRestController extends
        ServiceBasedRestController<Sample, Long, SampleResourceService> {

    @Override
    @Inject
    public void setService(SampleResourceService service) {
        this.service = service;
    }

}
