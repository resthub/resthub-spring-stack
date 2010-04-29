package org.resthub.core.domain.dao;

import java.io.Serializable;
import java.util.List;


public interface GenericDao<T, PK extends Serializable> extends org.synyx.hades.dao.GenericDao<T, PK>{
    
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
