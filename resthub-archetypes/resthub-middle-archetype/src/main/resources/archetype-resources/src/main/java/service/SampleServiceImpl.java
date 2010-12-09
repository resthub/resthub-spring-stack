#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.service;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.core.service.GenericResourceServiceImpl;
import ${package}.dao.SampleDao;
import ${package}.model.SampleResource;

@Named("sampleService")
public class SampleServiceImpl extends GenericResourceServiceImpl<SampleResource, SampleDao> implements SampleService {

    @Inject
    @Named("sampleDao")
    @Override
    public void setDao(SampleDao dao) {
        this.dao = dao;
    }

}
