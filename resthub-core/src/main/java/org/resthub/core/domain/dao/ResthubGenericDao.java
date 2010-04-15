package org.resthub.core.domain.dao;

import java.io.Serializable;
import java.util.List;

import org.synyx.hades.dao.GenericDao;
import org.synyx.hades.dao.NoDaoBean;

/**
 * Generic DAO interface.
 * 
 * @param <T>
 *            Resource class
 */
@NoDaoBean
public interface ResthubGenericDao<T, PK extends Serializable> extends
		GenericDao<T, PK> {

	/**
	 * Delete Persisted Entity by id.
	 * 
	 * @param id
	 *            Resource ID to delete
	 */
	void delete(PK id);

	/**
	 * Get all Resources (in scrollable resulset).
	 * 
	 * @param offset
	 *            offset
	 * @param limit
	 *            limit
	 * @return list of Resources.
	 */
	List<T> readAll(Integer offset, Integer limit);

}
