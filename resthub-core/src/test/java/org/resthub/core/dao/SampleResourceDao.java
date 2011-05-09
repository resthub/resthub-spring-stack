package org.resthub.core.dao;

import org.resthub.core.model.SampleResource;

/**
 * Resource DAO instance (not abstract)
 * It is instantiated as the Spring bean named resourceDao
 */
public interface SampleResourceDao extends GenericResourceDao<SampleResource> {

}
