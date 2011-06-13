#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.service;

import javax.inject.Named;
import javax.inject.Inject;

import org.resthub.core.service.GenericServiceImpl;
import ${package}.dao.SampleDao;
import ${package}.model.Sample;

@Named("sampleService")
public class SampleServiceImpl extends GenericServiceImpl<Sample, Long, SampleDao> implements SampleService {

    @Inject
    @Named("sampleDao")
    @Override
    public void setDao(SampleDao dao) {
        this.dao = dao;
    }

}
