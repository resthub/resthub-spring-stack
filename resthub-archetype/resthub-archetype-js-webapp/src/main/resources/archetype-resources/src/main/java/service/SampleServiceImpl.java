#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.service;

import javax.inject.Named;
import javax.inject.Inject;

import org.resthub.common.service.GenericServiceImpl;
import ${package}.repository.SampleRepository;
import ${package}.model.Sample;

@Named("sampleService")
public class SampleServiceImpl extends GenericServiceImpl<Sample, Long, SampleRepository> implements SampleService {

    @Inject
    @Named("sampleRepository")
    @Override
    public void setRepository(SampleRepository sampleRepository) {
        this.repository = sampleRepository;
    }
}
