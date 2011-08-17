package org.resthub.identity.repository;

import java.util.List;

import org.resthub.identity.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 
 * @author "Nicolas Morel <nicolas.morel@atosorigin.com>"
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

	/**
	 * Find a list of {@link Role} from name
	 * 
	 * @param name
	 *            name to search for
	 * 
	 * @return the list of found roles (empty if not found)
	 */
	List<Role> findByName(String name);

	/**
	 * Find the role with its name according to a pattern.
	 * 
	 * @param pattern
	 *            The pattern to look for.
	 * @return A list of roles corresponding to the pattern.
	 */
	List<Role> findByNameLike(String pattern);
}
