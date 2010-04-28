package org.resthub.core.domain.dao.jpa;

import javax.inject.Named;

import org.resthub.core.domain.dao.SampleResourceDao;
import org.resthub.core.domain.model.SampleResource;

@Named("sampleResourceDao")
public class SampleJpaResourceDao extends GenericJpaResourceDao<SampleResource> implements SampleResourceDao {

}
