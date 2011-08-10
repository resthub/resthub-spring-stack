package org.resthub.booking.repository;

import java.util.List;

import org.resthub.booking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author bmeurant <Baptiste Meurant>
 */
public interface UserRepository extends JpaRepository<User, Long> {

	/**
	 * Find a list of {@link User} from user name
	 * 
	 * @param username
	 *            name to search for
	 * 
	 * @return the list of found users (empty if not found)
	 */
	List<User> findByUsername(String username);

	/**
	 * Find a list of {@link User} from email
	 * 
	 * @param email
	 *            email to search for
	 * 
	 * @return the list of found users (empty if not found)
	 */
	List<User> findByEmail(String email);
}
