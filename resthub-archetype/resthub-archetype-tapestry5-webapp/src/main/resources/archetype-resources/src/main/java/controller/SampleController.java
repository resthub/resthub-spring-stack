#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.controller;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Path;

import org.resthub.web.controller.GenericControllerImpl;
import ${package}.model.Sample;
import ${package}.service.SampleService;

@Path("/sample")
@Named("sampleController")
public class SampleController extends GenericControllerImpl<Sample, Long, SampleService> {

    @Inject
    @Named("sampleService")
    @Override
    public void setService(SampleService service) {
        this.service = service;
    }
}
