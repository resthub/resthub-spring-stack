package org.resthub.core.dao;

import javax.inject.Named;

import org.resthub.core.dao.jpa.GenericJpaResourceDao;
import org.resthub.core.model.SampleResource;

@Named("sampleResourceDao")
public class SampleJpaResourceDao extends GenericJpaResourceDao<SampleResource> implements SampleResourceDao {

}
