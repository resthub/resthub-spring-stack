package org.resthub.core.domain.dao;

import java.util.List;

import org.resthub.core.domain.model.DerivedEntity;

/**
 * @author bmeurant <baptiste.meurant@gmail.com>
 * 
 * Test dedicated interface to provide specific methods for
 * {@link DerivedEntity} class.
 * 
 * This allows to validate that Resthub Genric Dao manages well resource
 * derived entities
 * 
 */
public interface DerivedEntityDao extends ResourceGenericDao<DerivedEntity> {

	/**
	 * Find a list of {@link DerivedEntity} from name
	 * 
	 * @param name
	 *            name to serach for
	 *            
	 * @return the list of found entities
	 */
	List<DerivedEntity> findByName(String name);

}
