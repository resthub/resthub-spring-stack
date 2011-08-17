package org.resthub.identity.repository;

import java.util.List;

import org.resthub.identity.model.AbstractPermissionsOwner;
import org.resthub.identity.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository for generic operations on AbstractPermissionsOwner entities.
 * 
 * @author "Nicolas Morel <nicolas.morel@atosorigin.com>"
 */
public interface AbstractPermissionsOwnerRepository extends JpaRepository<AbstractPermissionsOwner, Long> {

	/**
	 * Gets all the AbstractPermissionsOwners that have a role.
	 * 
	 * @param roles
	 *            A list of roles to look for.
	 * @return A list of AbstractPermissionsOwners having at least one of the roles defined as parameter.
	 */
	@Query("SELECT DISTINCT e FROM AbstractPermissionsOwner e JOIN e.roles r WHERE r.name IN :roles")
	List<AbstractPermissionsOwner> getWithRoles(@Param("roles") List<String> roles);

	/**
	 * Gets all the AbstractPermissionsOwners that have the specified group as parent.
	 * 
	 * @param group
	 *            The parent group.
	 * @return The list of AbstractPermissionsOwners that are associated with the group.
	 */
	@Query("SELECT DISTINCT e FROM AbstractPermissionsOwner e JOIN e.groups g WHERE g = :group")
	List<AbstractPermissionsOwner> getWithGroupAsParent(@Param("group") Group group);
}
