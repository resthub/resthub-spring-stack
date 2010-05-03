package org.resthub.core.domain.dao;

import java.io.Serializable;
import java.util.List;

/**
 * Generic Dao that can manage any kind of entities.
 * Extends <a href="http://hades.synyx.org/static/2.x/site/org.synyx.hades/apidocs/org/synyx/hades/dao/GenericDao.html">Hades GenericDao</a>.
 * 
 * @see <a href="http://hades.synyx.org/static/2.x/site/org.synyx.hades/apidocs/" target="_blank">Hades 2.0 Javadoc</a>
 */
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
