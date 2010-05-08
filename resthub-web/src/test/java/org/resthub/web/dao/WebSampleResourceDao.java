package org.resthub.web.dao;

import org.resthub.core.dao.GenericResourceDao;
import org.resthub.web.model.WebSampleResource;

/**
 * Resource DAO instance (not abstract)
 * It is instantiated as the Spring bean named resourceDao
 */
public interface WebSampleResourceDao extends GenericResourceDao<WebSampleResource> {

}
