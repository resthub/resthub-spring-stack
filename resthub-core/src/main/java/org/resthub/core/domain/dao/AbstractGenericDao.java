package org.resthub.core.domain.dao;

import java.io.Serializable;
import java.util.List;

import org.synyx.hades.dao.GenericDao;
import org.synyx.hades.dao.NoDaoBean;

/**
 * Abstract Generic DAO
 *  
 * RESThub Generic DAO interface that can persist every kind of entities (not linked to Resource classes).
 * It extends Hades GenericDao to add a few useful methods.
 * 
 * This interface is considered as abstract (@NoDaoBean annotation) because it doesn't create a Spring bean
 * Bean creation will be done on inherited interfaces.
 */
@NoDaoBean
public interface AbstractGenericDao<T, PK extends Serializable> extends
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
