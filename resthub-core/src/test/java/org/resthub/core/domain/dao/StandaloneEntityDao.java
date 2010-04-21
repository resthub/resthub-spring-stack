package org.resthub.core.domain.dao;

import java.util.List;

import org.resthub.core.domain.model.StandaloneEntity;

/**
 * @author bmeurant <baptiste.meurant@gmail.com>
 * 
 * Test dedicated interface to provide specific methods for
 * {@link StandaloneEntity} class.
 * 
 * This allows to validate that Resthub Genric Dao manages well non resource
 * derived entities
 * 
 */
public interface StandaloneEntityDao extends AbstractResthubGenericDao<StandaloneEntity, Long> {

	/**
	 * Find a list of {@link StandaloneEntity} from name
	 * 
	 * @param name
	 *            name to serach for
	 *            
	 * @return the list of found entities
	 */
	List<StandaloneEntity> findByName(String name);

}
