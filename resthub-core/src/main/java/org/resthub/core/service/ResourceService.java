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

package org.resthub.core.service;

import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.util.List;

import org.resthub.core.domain.model.Resource;
import org.resthub.core.exception.ResthubException;

/**
 * Provides Business Services to manipulate Resource objets.
 * Secured annotation are provided by Spring Security and are directly used by GraniteDS to control user rights
 * 
 * Since ResourceService is used for all kind of resources, instances are configured 
 * in the appCtxGlobal.xml file
 *  
 * @author Bouiaw
 */
public interface ResourceService {
	
	/**
	 * @return the list of the resources stored in the repository
	 */
	List<Resource> findAll() throws ResthubException;
	
	/**
	 * Retreive a resource from its path
	 * @return the resource if it exists, else null
	 */
	Resource retreive(String path) throws ResthubException;
	
	/**
	 * Retreive a resource from its short path, ie without children nodes
	 * @return the resource if it exists, else null
	 */
	Resource retreiveFromShortPath(String path) throws ResthubException;
	
	
	/**
	 * Create a new resource in the resource root
	 * @return the created resource, that could be upated by its insertion in the repository
	 */
	Resource create(Resource resource) throws ResthubException;	
	
	/**
	 * Update a resource
	 * @return the updated resource
	 */
	Resource update(Resource resource) throws ResthubException;
	
	/**
	 * Move a resource
	 * @return the moved resource
	 */
	Resource move(Resource resource, Resource parent) throws ResthubException;
	
	/**
	 * Update resources
	 * @return updated resources
	 */
	List<Resource> updateAll(List<Resource> resources) throws ResthubException;
	
	/**
	 * Remove a resource
	 * @return remove resource
	 */
	Resource remove(Resource resource) throws ResthubException;
	
	/**
	 * Remove resources
	 * @resturn removed resources
	 */
	List<Resource> removeAll(List<Resource> resources) throws ResthubException;
	
	/**
	 * Check if a resource exists
	 * @return true if the resource exists, else false
	 */
	boolean exists(String name) throws ResthubException;
	
	/**
	 * Import and replace existing resources in the Java Content Reposiory
	 * @param path the root path where we should import resources
	 * @param inputStream the stream that will deliver the zip file to import
	 */
	void importResources(InputStream inputStream) throws ResthubException;
	
	/**
	 * Export resources from the Java Content Repository
	 * @param path the root path to export
	 * @param bufferedOutputStream the stream that will receive the zip file export
	 */
	void exportResources(BufferedOutputStream bufferedOutputStream) throws ResthubException;
	
	/**
	 * Find resources of a specified type
	 * @param rootPath  the root where we should search
	 * @param type the Java classname of the resources to find
	 * @return Found resources
	 */
	List<Resource> findByType(String type) throws ResthubException;
	
	/**
	 * Find resources of a specified type
	 * @param rootPath  the root where we should search
	 * @param An array of types matching the Java classnames of the resources to find, may contain % joker
	 * @return Found resources
	 */
	List<Resource> findByTypes(List<String> types) throws ResthubException;
	
	/**
	 * Move resources
	 * @return moved resources
	 */
	List<Resource> moveAll(List<Resource> resources, Resource parent) throws ResthubException;
	
	/**
     * Map model classes based on their package name
     */
	public void setMappedPackage(String packageName);

	/**
     * Map model classes based on their package name
     */	
	public void setMappedPackages(List<String> packageNames);
	
}
