package org.resthub.core.domain.dao;

import java.util.List;

import org.resthub.core.domain.model.Resource;
import org.synyx.hades.dao.NoDaoBean;

/**
 * Resource oriented abstract Generic DAO
 * 
 * This interface is considered as abstract (@NoDaoBean annotation) because it doesn't create a Spring bean
 * Bean creation will be done on inherited interfaces.
 * 
 * @see org.resthub.core.domain.model.Resource
 * @see org.resthub.core.domain.dao.AbstractGenericDao
 */
@NoDaoBean
public interface AbstractResourceGenericDao<T extends Resource> extends
		AbstractGenericDao<T, Long> {
	
	List<T> findByRef(String ref);

}
