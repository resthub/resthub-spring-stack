package org.resthub.web.domain.dao;

import org.resthub.core.domain.dao.GenericResourceDao;
import org.resthub.web.domain.model.WebSampleResource;

/**
 * Resource DAO instance (not abstract)
 * It is instantiated as the Spring bean named resourceDao
 */
public interface WebSampleResourceDao extends GenericResourceDao<WebSampleResource> {

}
