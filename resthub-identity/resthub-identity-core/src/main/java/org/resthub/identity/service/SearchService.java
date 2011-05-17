package org.resthub.identity.service;

import java.util.List;

import org.resthub.identity.model.AbstractPermissionsOwner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Special service that performs search queries on users, groups and roles
 */
@Transactional(readOnly=true)
public interface SearchService {

	/**
	 * Performs a cross search on every fields of users, groups and roles classes.
	 *  
	 * @param query The Lucene search query.
	 * @param withUsers True to search on users.
	 * @param withGroups True to search on groups.
	 * @return List of matching resources, that may be empty but not null.
	 * 
	 * @throws IllegalArgumentException If the query is invalid of misformated
	 */
	List<AbstractPermissionsOwner> search(String query, boolean withUsers, boolean withGroups);
	
	/**
	 * Re-index existing groups, users and roles.
	 */
	void resetIndexes();

} // interface SearchService
