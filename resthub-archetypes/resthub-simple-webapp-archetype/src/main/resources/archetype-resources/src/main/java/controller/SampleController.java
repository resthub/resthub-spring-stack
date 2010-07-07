#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.controller;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Path;

import ${package}.model.SampleResource;
import ${package}.service.SampleService;
import org.resthub.web.controller.GenericResourceController;

@Path("/sample")
@Named("sampleController")
public class SampleController extends GenericResourceController<SampleResource, SampleService> {

    @Inject
    @Named("sampleService")
    @Override
    public void setService(SampleService service) {
        this.service = service;
    }
}
