#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.service;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.common.util.PostInitialize;

import ${package}.model.Sample;

@Named("sampleInitializer")
public class SampleInitializer {

    @Inject
    @Named("sampleService")
    private SampleService sampleService;

    @PostInitialize
    public void init() {
        Sample s = new Sample("testSample");
        sampleService.create(s);
    }
}
