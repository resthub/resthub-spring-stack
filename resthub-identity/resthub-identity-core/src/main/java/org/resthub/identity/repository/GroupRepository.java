package org.resthub.identity.repository;

import java.util.List;

import org.resthub.identity.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {

	/**
	 * Find a list of {@link Group} from name
	 * 
	 * @param name
	 *            name to search for
	 * 
	 * @return the list of found Group (empty if not found)
	 */
	List<Group> findByName(String name);
}
