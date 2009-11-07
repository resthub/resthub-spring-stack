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

package org.resthub.core.service.impl;

import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.resthub.core.domain.dao.ResourceDao;
import org.resthub.core.domain.model.Resource;
import org.resthub.core.exception.ResthubException;
import org.resthub.core.exception.ResourceAlreadyExistsException;
import org.resthub.core.service.ResourceService;
import org.resthub.core.utils.JcrUtils;
import org.jcrom.JcrMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the ResourceService
 * @author Bouiaw
 */

@Service("resourceService" )
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class ResourceServiceImpl implements ResourceService {

	/**
	 * Logger 
	 */
	private static Logger logger = LoggerFactory.getLogger(ResourceServiceImpl.class);

	@Autowired
	private ResourceDao resourceDao;

	/**
	 * Resource root name
	 */
	private String rootName = "";

	/**
	 *  Initializes resource root if it does not exists
	 *  Create all subnodes if needed
	 */
	public void initializeRoot() {
		try {
			
			if (!resourceDao.exists(JcrUtils.createPath(rootName))) {
				String[] rootNodes = rootName.split("/");
				boolean isSubNode = false;
				boolean isNewRoot = false;
				Resource root = null;
				Resource lastResource = null;
				for(String node : rootNodes) {
					if((node.length() > 0) && (!node.equals("children"))) {
						if(isSubNode==false) {
							if(resourceDao.exists(JcrUtils.createPath(node))) {
								root = (Resource)resourceDao.get(JcrUtils.createPath(node));
							} else {
								root = new Resource(node);
								isNewRoot = true;
							}
							
							lastResource = root;
							isSubNode = true;
						} else {
							Resource subNode = new Resource(node);
							lastResource.addChild(subNode);
							lastResource = subNode;
						}
					}
				}
				if(isNewRoot)
					resourceDao.create("/", root);
				else
					resourceDao.update(root);
			}
			
		} catch (Exception e) {
			logger.error("Error during root initialization for " + rootName + " : " + e.getCause());
		}
	}

	/**
	 * {@inheritDoc}
	 * @throws Exception 
	 */
	@Transactional(readOnly = false)
	public Resource create(Resource resource) throws ResthubException {

		initializeRoot();
		
		Resource newResource = null;
		
		String root = getRootPath();
		if(resource.getParent() != null) {
			root = resource.getParent().getPath();
		}		
		
		if (resourceDao.exists(JcrUtils.createPath(root, resource.getName()))) {
			throw new ResourceAlreadyExistsException();
		}
		
		Resource parent = resource.getParent();
		
		if(parent==null) {
			newResource = (Resource)resourceDao.create(JcrUtils.createPath(getRootPath()), resource);
		} else {
			newResource = (Resource)resourceDao.create(JcrUtils.createPath(resource.getParent().getPath(), "children"), resource);
			// Sometimes, Jcrom forgot parent, so we remember it
			if ((newResource.getParent() == null) && (parent != null)) {
				newResource.setParent(parent);
			}
		}

		return (Resource)newResource;
	}

	/**
	 * {@inheritDoc}
	 * @throws Exception 
	 */
	public Resource retreive(String path) throws ResthubException {
		if (!path.startsWith(getRootPath())) {
			path = JcrUtils.createPath(getRootPath(), path);
		}
		return (Resource)resourceDao.get(path);
	}
	
	/**
	 * {@inheritDoc}
	 * @throws Exception 
	 */
	public Resource retreiveFromShortPath(String path) {
		return this.retreive(JcrUtils.convertShortPath(path));
	}

	/**
	 * {@inheritDoc}
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public List<Resource> findAll() throws ResthubException {
		List<Resource> resources = null;
		try {
			resources = (List)resourceDao.findAll(JcrUtils.createPath(getRootPath()));
		} catch (JcrMappingException e) {
			logger.debug("Path not found for resource root" + e);
		}

		return resources;
	}

	/**
	 * {@inheritDoc}
	 * @see org.resthub.core.service.ResourceService#exists(java.lang.String)
	 */
	public boolean exists(String name) throws ResthubException {
		return resourceDao.exists(JcrUtils.createPath(getRootPath(), name));
	}

	/**
	 * {@inheritDoc}
	 * @see org.resthub.core.service.ResourceService#update(org.resthub.core.domain.model.Resource)
	 */
	@Transactional(readOnly = false)
	public Resource update(Resource resource) throws ResthubException {
		resourceDao.updateByUUID(resource, resource.getUid(), "*", -1);
		return (Resource)resourceDao.loadByUUID(resource.getUid());
	}
	
	/**
	 * {@inheritDoc}
	 * @see org.resthub.core.service.ResourceService#update(org.resthub.core.domain.model.Resource)
	 */
	@Transactional(readOnly = false)
	public Resource move(Resource resource, Resource parent) throws ResthubException {
		String parentPath;
		if (parent == null) {
			parentPath = JcrUtils.createPath(rootName);
		}
		else {
			parentPath = parent.getPath();
		}
		resourceDao.move(resource, JcrUtils.createPath(parentPath, "children"));
		return (Resource)resourceDao.loadByUUID(resource.getUid());
	}

	/**
	 * {@inheritDoc}
	 * @see org.resthub.core.service.ResourceService#remove(org.resthub.core.domain.model.Resource)
	 */
	@Transactional(readOnly = false)
	public Resource remove(Resource resource) throws ResthubException {
		resourceDao.removeByUUID(resource.getUid());
		return resource;
	}

	/**
	 * {@inheritDoc}
	 * @see org.resthub.core.service.ResourceService#removeAll(org.resthub.core.domain.model.Resource)
	 */
	public List<Resource> removeAll(List<Resource> resources) throws ResthubException {
		
		List<Resource> results = new ArrayList<Resource> ();
		
		for (Resource resource : resources) {
			results.add(remove(resource));
		}
		
		return results;
	}
	
	/**
	 * Update resources
	 * @return updated resources
	 */
	public List<Resource> updateAll(List<Resource> resources) throws ResthubException {
		
		List<Resource> results = new ArrayList<Resource> ();
		
		for (Resource resource : resources) {
			results.add(update(resource));
		}
		
		return results;
	}
	
	/**
	 * Move resources
	 * @return moved resources
	 */
	public List<Resource> moveAll(List<Resource> resources, Resource parent) throws ResthubException {
		
		List<Resource> results = new ArrayList<Resource> ();
		
		for (Resource resource : resources) {
			results.add(move(resource, parent));
		}
		
		return results;
	}
	

	public String getRootName() {
		return rootName;
	}

	public void setRootName(String rootName) {
		this.rootName = rootName;
	}

	public String getRootPath() {
		return this.rootName + "/children";
	}
	
    /**
	 * {@inheritDoc}
	 */
	public void importResources(InputStream inputStream) throws ResthubException {
		resourceDao.importResources(this.rootName, inputStream);
	}
	
    /**
	 * {@inheritDoc}
	 */
	public void exportResources(BufferedOutputStream bufferedOutputStream) throws ResthubException {
		resourceDao.exportResources(this.rootName, bufferedOutputStream);
	}
	
    /**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public List<Resource> findByType(String type) throws ResthubException {
		return (List)resourceDao.findByType(rootName, type);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public List<Resource> findByTypes(List<String> types) throws ResthubException {
		return (List)resourceDao.findByTypes(rootName, types);
	}
	
	/**
	 * {@inheritDoc}
	 */
    public void setMappedPackage(String packageName) {
    	resourceDao.mapPackage(packageName);
    }
    
	/**
	 * {@inheritDoc}
	 */
    public void setMappedPackages(List<String> packageNames) {
    	for(String packageName: packageNames) {
    		resourceDao.mapPackage(packageName);
    	}
    }

}
