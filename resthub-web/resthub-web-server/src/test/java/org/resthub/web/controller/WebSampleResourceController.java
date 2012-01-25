package org.resthub.web.controller;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.web.model.WebSampleResource;
import org.resthub.web.service.WebSampleResourceService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller @RequestMapping("/resources")
public class WebSampleResourceController extends
        GenericControllerImpl<WebSampleResource, Long, WebSampleResourceService> {

    @Inject
    @Named("webSampleResourceService")
    @Override
    public void setService(WebSampleResourceService service) {
        this.service = service;
    }

}
