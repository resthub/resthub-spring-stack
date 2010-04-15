package org.resthub.core.domain.dao;

import org.resthub.core.domain.model.Resource;
import org.synyx.hades.dao.NoDaoBean;

/**
 * Generic Resource DAO interface.
 * 
 * @param <T>
 *            Resource class
 */
@NoDaoBean
public interface ResourceGenericDao<T extends Resource> extends
		ResthubGenericDao<T, Long> {

}
