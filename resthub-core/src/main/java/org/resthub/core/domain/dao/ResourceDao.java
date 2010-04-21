package org.resthub.core.domain.dao;

import org.resthub.core.domain.model.Resource;

/**
 * Resource DAO instance (not abstract)
 * It is instantiated as the Spring bean named resourceDao
 */
public interface ResourceDao extends AbstractResourceGenericDao<Resource> {

}
