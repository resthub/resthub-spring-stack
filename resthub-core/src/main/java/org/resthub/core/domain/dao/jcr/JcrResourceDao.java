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

package org.resthub.core.domain.dao.jcr;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.jcr.ImportUUIDBehavior;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Workspace;

import org.resthub.core.domain.dao.ResourceDao;
import org.resthub.core.domain.model.IResource;
import org.resthub.core.exception.ResthubException;
import org.jcrom.Jcrom;
import org.jcrom.dao.AbstractJcrDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.extensions.jcr.SessionFactory;
import org.springframework.extensions.jcr.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

/**
 * Java Content Repository implementation of {@link ResourceDao} with Jcrom.
 * @author Bouiaw
 */
@Repository("resourceDao")
public class JcrResourceDao extends AbstractJcrDAO<IResource> implements ResourceDao {
    
	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(JcrResourceDao.class);
	
	private SessionFactory jcrSessionFactory; 
	
	/**
	 * Create a JcrResourceDao instance
	 * @param sessionFactory JCR Session factory
	 * @param jcrom Jcrom main class
	 * @throws RepositoryException
	 */
	@Autowired
	public JcrResourceDao(SessionFactory jcrSessionFactory, Jcrom jcrom) throws RepositoryException {
		/* construct session null to allow manage session outside jcrom */    
		super(IResource.class, null, jcrom);
		this.jcrSessionFactory = jcrSessionFactory; 
    }

	
    @Override
    protected Session getSession() {
        if (jcrSessionFactory != null) {
            return
            	SessionFactoryUtils.getSession(this.jcrSessionFactory, true);
        } else {
            return super.getSession();
        }
    }
    
    /**
	 * {@inheritDoc}
	 */
    public void mapPackage(String packageName) {
    	jcrom.mapPackage(packageName, true);
    }

    /**
	 * {@inheritDoc}
	 */
    public void importResources(String path, InputStream inputStream) {
    	Workspace workspace = getSession().getWorkspace();
    	try {
    		workspace.importXML(path, inputStream, ImportUUIDBehavior.IMPORT_UUID_COLLISION_REPLACE_EXISTING);
		} catch (Exception e) {
			throw new ResthubException("Error during import to path " + path, e);
		}
    }
   
    /**
	 * {@inheritDoc}
	 */
    public void exportResources(String path, OutputStream outputStream) {
    	Session session = getSession();
    	try {
    		if(exists(path)) {
    			session.exportSystemView(path, outputStream, false, false);
    		}
    	} catch (Exception e) {
			throw new ResthubException("Error during export to path " + path, e);
		}

    }
    
    /**
	 * {@inheritDoc}
	 */
    public List<IResource> findByType(String rootPath, String type) {
    	String xPathRoot = null;
    	if((rootPath==null) || rootPath.equals(""))
    		xPathRoot = "//*";
    	else
    		xPathRoot = "/jcr:root" + rootPath + "//element(*, nt:unstructured)";
    	return this.findByXPath(xPathRoot + "[jcr:like(@className,'" + type +"')]", "*", -1);
    }
    
    public List<IResource> findByTypes(String rootPath, List<String> types) {
    	StringBuilder xPathRequestBuilder = new StringBuilder();
    	
    	// Root path
    	if((rootPath==null) || rootPath.equals(""))
    		xPathRequestBuilder.append("//*");
    	else
    		xPathRequestBuilder.append("/jcr:root" + rootPath + "//element(*, nt:unstructured)");
    	
    	// WHERE clause
    	xPathRequestBuilder.append("[");
    	for(int i=0;i<types.size();i++) {
    		xPathRequestBuilder.append("jcr:like(@className,'" + types.get(i) +"')");
    		if(i!=types.size() - 1)
    			xPathRequestBuilder.append(" or ");
    	}
    	xPathRequestBuilder.append("]");
    	
    	return this.findByXPath(xPathRequestBuilder.toString(), "*", -1);
    }

}
