package org.resthub.core.repository;

import java.util.List;

import org.resthub.core.model.StandaloneEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Test dedicated interface to provide specific methods for
 * {@link StandaloneEntity} class.
 * 
 * This allows to validate that Respository manages well entities
 */
public interface StandaloneEntityRepository extends JpaRepository<StandaloneEntity, Long> {

	/**
	 * Find a list of {@link StandaloneEntity} from name
	 * 
	 * @param name
	 *            name to serach for
	 * 
	 * @return the list of found entities (empty if not found)
	 */
	List<StandaloneEntity> findByName(String name);
}
