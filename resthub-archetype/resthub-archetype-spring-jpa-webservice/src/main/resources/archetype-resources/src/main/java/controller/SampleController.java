#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.controller;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.web.controller.ServiceBasedRestController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import ${package}.model.Sample;
import ${package}.service.SampleService;

@Controller 
@RequestMapping(value = "/api/sample")
public class SampleController extends ServiceBasedRestController<Sample, Long, SampleService> {

    @Inject
    @Named("sampleService")
    @Override
    public void setService(SampleService service) {
        this.service = service;
    }

    @Override
    public Long getIdFromResource(Sample resource) {
        return resource.getId();
    }
}
