#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.test;


import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.core.test.service.AbstractServiceTest;
import ${package}.model.Sample;
import ${package}.service.SampleService;

public class SampleServiceTest extends AbstractServiceTest<Sample, Long, SampleService> {

    @Inject
    @Named("sampleService")
    @Override
    public void setService(SampleService service) {
        super.setService(service);
    }

    @Override
    public void testUpdate() throws Exception {
        // Not implemented yet
    }
   
}
