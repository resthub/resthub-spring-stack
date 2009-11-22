/**
 *  This file is part of Resthub.
 *
 *  Resthub is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *   Resthub is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Resthub.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.resthub.core.domain.dao;

import java.util.List;

import org.resthub.core.domain.model.Resource;

/**
 * ResourceDao is used for CRUD operation on {@link Resource}.
 * 
 * @author Bouiaw
 */
public interface ResourceDao <T> {
	
	/**
	 * Persist a Resource entity in the database
	 * 
	 * @param transientResource
	 */
	public void persist(T transientResource);
	public void persistAndFlush(T transientResource);

	/**
	 * Remove a persisted Resource from the database
	 * 
	 * @param persistentResource
	 */
	public void remove(T persistentResource);
	
	/**
	 * Remove a persisted Resource from the database
	 * 
	 * @param resourceId
	 */
	public void remove(Long resourceId);

	/**
	 * Update a Resource in the database
	 * 
	 * @param detachedResource
	 * @return merged Resource
	 */
	public T merge(T detachedResource);

	/**
	 * Find a Resource by id
	 * 
	 * @param id
	 * @return the found Resource
	 */
	public T findById(Long id);
	
	/**
	 * Find a Resource by path
	 * 
	 * @param fullname
	 * @return the found Resource
	 */
	public T findByPath(String path);

	/**
	 * Find a Resource by his fullname
	 * 
	 * @return the found Resource
	 */
	public List<T> findAll();

	/**
	 * Search Resources
	 * 
	 * @param searchString
	 * @return the found Resources
	 */
	public List<T> search(String searchString);
	
	public T findByName(String name);
	public T findSingleResult(String propertyName, String propertyValue);
	public List<T> findMultipleResults(String propertyName, String propertyValue);
	
}
