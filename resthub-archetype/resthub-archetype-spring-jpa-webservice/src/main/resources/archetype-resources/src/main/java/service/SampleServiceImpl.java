#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.service;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.common.service.RepositoryBasedRestService;

import ${package}.model.Sample;
import ${package}.repository.SampleRepository;

@Named("sampleService")
public class SampleServiceImpl extends RepositoryBasedRestService<Sample, Long, SampleRepository> implements SampleService {

    @Inject
    @Named("sampleRepository")
    @Override
    public void setRepository(SampleRepository sampleRepository) {
        this.repository = sampleRepository;
    }
}
