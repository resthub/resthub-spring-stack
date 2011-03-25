package org.resthub.identity.dao;

import java.util.List;

import org.resthub.core.model.Resource;

/**
 * Special DAO that performs search queries on users, groups and roles
 */
public interface SearchDao {

	/**
	 * Performs a cross search on every fields of users, groups and roles classes.
	 *  
	 * @param query The Lucene search query.
	 * @param withUsers True to search on users.
	 * @param withGroups True to search on groups.
	 * @param withRoles True to search on roles.
	 * @return List of matching resources, that may be empty but not null.
	 * 
	 * @throws IllegalArgumentException If the query is invalid 
	 */
	List<Resource> search(String query, boolean withUsers, boolean withGroups, boolean withRoles);
	
	/**
	 * Re-index existing groups, users and roles.
	 */
	void resetIndexes();

} // interface SearchDao
