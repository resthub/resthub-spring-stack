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

import static org.junit.Assert.assertEquals;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.resthub.core.domain.model.IResource;
import org.resthub.core.domain.model.Resource;
import org.resthub.test.AbstractJcrTest;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Test cases for FolderDao
 * @author Bouiaw
 */

public class ResourceDaoTest extends AbstractJcrTest {
	
	/**
	 * The FolderDao to test 
	 */
	@Autowired
	private ResourceDao resourceDao;
	
	/**
	 * Logger 
	 */
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(ResourceDaoTest.class);
	
	/**
	 * Test empty resource creation 
	 */
	@Test
	public void testCreateEmpty() throws Exception {		
		
		IResource rootResource = new Resource("resource1");
		rootResource = resourceDao.create("/", rootResource);
		
		rootResource = resourceDao.get(rootResource.getPath());
		assertEquals("resource1", rootResource.getName());
		
	}
	
	/**
	 * Test a 2 levels resource tree 
	 */
	@Test
	public void testCreateTree() throws Exception {		
		
		IResource rootResource = new Resource("resource2");
		resourceDao.create("/", rootResource);
		
		IResource childResource = new Resource("resource3");
		resourceDao.create("/resource2", childResource);
		
		rootResource = resourceDao.get("/resource2");
		assertEquals("resource2", rootResource.getName());
		
		childResource = resourceDao.get("/resource2/resource3");
		assertEquals("resource3", childResource.getName());
		
	}
	
	@Test
	public void testExport() throws Exception {		
		testCreateTree();
		
		File exchangeFile = new File("./target/exchangeFile.xml");
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(exchangeFile));
		resourceDao.exportResources("/resource2", bufferedOutputStream);
		bufferedOutputStream.close();
		
	}

	
	@Test
	public void testImport() throws Exception {		
		File exchangeFile = new File("./target/exchangeFile.xml");
		resourceDao.importResources("/", new FileInputStream(exchangeFile));
		IResource rootResource = resourceDao.get("/resource2");
		assertEquals("resource2", rootResource.getName());
		IResource childResource = resourceDao.get("/resource2/resource3");
		assertEquals("resource3", childResource.getName());
	}
	

}
