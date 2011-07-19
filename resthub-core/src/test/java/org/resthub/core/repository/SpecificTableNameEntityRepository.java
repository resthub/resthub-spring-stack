package org.resthub.core.repository;

import java.util.List;

import org.resthub.core.model.SpecificTableNameEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Class used to test dedicated interface to provide specific methods for
 * {@link SpecificTableNameEntity} class.
 * 
 * This allows to validate that Repository manages well entities named
 * differently than their classes
 */
public interface SpecificTableNameEntityRepository extends JpaRepository<SpecificTableNameEntity, Long> {

	/**
	 * Find a list of {@link SpecificTableNameEntity} from name
	 * 
	 * @param name
	 *            name to serach for
	 * 
	 * @return the list of found entities (empty if not found)
	 */
	List<SpecificTableNameEntity> findByName(String name);
}
