#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.service;

import javax.inject.Named;
import javax.inject.Inject;
import org.resthub.core.util.PostInitialize;
import ${package}.model.Sample;

@Named("sampleInit")
public class SampleInit {
    
    @Inject
    @Named("sampleService")
    private SampleService sampleService;
    
    @PostInitialize
    public void init() {
        Sample s = new Sample();
        s.setName("testSample");
        sampleService.create(s);
    }   

}
