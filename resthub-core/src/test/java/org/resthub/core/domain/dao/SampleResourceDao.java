package org.resthub.core.domain.dao;

import org.resthub.core.domain.model.SampleResource;

/**
 * Resource DAO instance (not abstract)
 * It is instantiated as the Spring bean named resourceDao
 */
public interface SampleResourceDao extends GenericResourceDao<SampleResource> {

}
