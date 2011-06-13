package org.resthub.identity.dao;

import java.util.List;

import org.resthub.identity.model.AbstractPermissionsOwner;

/**
 * Special DAO that performs search queries on users, groups and roles
 */
public interface SearchDao {

    /**
     * Performs a cross search on every fields of users, groups and roles
     * classes.
     * 
     * @param query
     *            The Lucene search query.
     * @param withUsers
     *            True to search on users.
     * @param withGroups
     *            True to search on groups.
     * @return List of matching resources, that may be empty but not null.
     * 
     * @throws IllegalArgumentException
     *             If the query is invalid
     */
    List<AbstractPermissionsOwner> search(String query, boolean withUsers, boolean withGroups);

    /**
     * Re-index existing groups, users and roles.
     */
    void resetIndexes();

} // interface SearchDao
