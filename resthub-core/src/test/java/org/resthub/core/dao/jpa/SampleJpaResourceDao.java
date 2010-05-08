package org.resthub.core.dao.jpa;

import javax.inject.Named;

import org.resthub.core.dao.SampleResourceDao;
import org.resthub.core.dao.jpa.GenericJpaResourceDao;
import org.resthub.core.model.SampleResource;

@Named("sampleResourceDao")
public class SampleJpaResourceDao extends GenericJpaResourceDao<SampleResource> implements SampleResourceDao {

}
