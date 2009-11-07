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

package org.resthub.core;

import java.io.InputStream;

import javax.jcr.RepositoryException;
import javax.jcr.Workspace;

import org.apache.jackrabbit.api.JackrabbitNodeTypeManager;
import org.apache.jackrabbit.core.nodetype.NodeTypeManagerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.extensions.jcr.JcrSessionFactory;

/**
 * Custom JCR factory used to provide improved registering for nodetypes
 * 
 * @author Bouiaw
 */
public class JackrabbitSessionFactory extends JcrSessionFactory {
	
	/**
	 * Logger 
	 */
	@SuppressWarnings("unused")
	private Logger logger = LoggerFactory.getLogger(JackrabbitSessionFactory.class);

	/**
	 * Node definitions in XML format.
	 */
	private Resource nodeDefinition;


	/**
	 * Improved registering for node type that work even if the nodetypes are already
	 * registered in the repository.
	 */
	protected void registerNodeTypes() throws Exception {
		try {
			InputStream xml = nodeDefinition.getInputStream();
			
			Workspace workspace = getSession().getWorkspace();
			NodeTypeManagerImpl ntMgr = (NodeTypeManagerImpl)workspace.getNodeTypeManager();
			ntMgr.registerNodeTypes(xml, JackrabbitNodeTypeManager.TEXT_XML,true);

		} catch (Exception e) {
			throw new RepositoryException("Impossible to register node types", e);
		}
	}

	/**
	 * @param nodeDefinition The nodeDefinition to set.
	 */
	public void setNodeDefinition(Resource nodeDefinition) {
		this.nodeDefinition = nodeDefinition;
	}

}
