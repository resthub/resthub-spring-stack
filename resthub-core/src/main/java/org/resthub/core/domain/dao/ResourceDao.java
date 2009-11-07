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

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.resthub.core.domain.model.IResource;
import org.resthub.core.domain.model.Resource;
import org.jcrom.dao.JcrDAO;

/**
 * ResourceDao is used for CRUD operation on {@link Resource}.
 * 
 * @author Bouiaw
 */
public interface ResourceDao extends JcrDAO<IResource> {
	
	/**
	 * Import and replace existing resources in the Java Content Reposiory
	 * @param path the root path where we should import resources
	 * @param inputStream the stream that will deliver the zip file to import
	 */
	public void importResources(String path, InputStream inputStream);
	
	
	/**
	 * Export resources from the Java Content Repository
	 * @param path the root path to export
	 * @param bufferedOutputStream the stream that will receive the zip file export
	 */
	public void exportResources(String path, OutputStream bufferedOutputStream);
	
	/**
	 * Find resources of a specified type
	 * @param rootPath  the root where we should search
	 * @param type the Java classname of the resources to find, may contain % joker
	 * @return Found resources
	 */
	public List<IResource> findByType(String rootPath, String type);
	
	/**
	 * Find resources of a specified type
	 * @param rootPath  the root where we should search
	 * @param An array of types matching the Java classnames of the resources to find, may contain % joker
	 * @return Found resources
	 */
	public List<IResource> findByTypes(String rootPath, List<String> types);
	
	/**
     * Map model classes based on their package name
     */
	public void mapPackage(String packageName);
	
}
