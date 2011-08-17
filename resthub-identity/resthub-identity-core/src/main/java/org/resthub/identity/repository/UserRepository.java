package org.resthub.identity.repository;

import java.util.List;

import org.resthub.identity.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author "Nicolas Morel <nicolas.morel@atosorigin.com>"
 */
public interface UserRepository extends JpaRepository<User, Long> {

	/**
	 * Find a list of {@link User} from login
	 * 
	 * @param login
	 *            login to search for
	 * 
	 * @return the list of found users (empty if not found)
	 */
	List<User> findByLogin(String login);

	/**
	 * Gets the user of a group.
	 * 
	 * @param groupName
	 *            The name of the group.
	 * @return A list of users corresponding to the given group.
	 */
	@Query("SELECT DISTINCT u FROM User u JOIN u.groups g WHERE g.name = :groupName")
	List<User> getUsersFromGroup(@Param("groupName") String groupName);
}
