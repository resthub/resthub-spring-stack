#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.test;


import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.core.test.AbstractResourceServiceTest;
import ${package}.model.SampleResource;
import ${package}.service.SampleService;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(locations = { "classpath*:resthubContext.xml", "classpath:${artifactId}Context.xml" })
public class SampleServiceTest extends AbstractResourceServiceTest<SampleResource, SampleService> {

    @Inject
    @Named("sampleService")
    @Override
    public void setResourceService(SampleService service) {
        super.setResourceService(service);
    }

    @Override
    public void testUpdate() throws Exception {
        // Not implemented yet
    }
   
}
