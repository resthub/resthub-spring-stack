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


import junit.framework.Assert;

import org.resthub.core.AbstractJcrTest;
import org.resthub.core.domain.model.Content;
import org.resthub.core.domain.model.identity.User;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Test cases for ContentDao
 * @author Bouiaw
 */
public class ContentDaoTest extends AbstractJcrTest {
	
	/**
	 * The content DAO to test 
	 */
	@javax.annotation.Resource(name = "resourceDao")
	private ResourceDao resourceDao;
	
	
	/**
	 * Logger 
	 */
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(ContentDaoTest.class);
	
	/**
	 * Tests creation, modification, reading and deletion of a Content
	 * @throws Exception 
	 */
	@Test
	public void testCreateContent() throws Exception {		
	    
		User pastis = new User("pastis");
		pastis.setPassword("toto");
        
		Content joconde = new Content("joconde");
		joconde.setTitle("Joconde");
		String body1 = "This is the first body of the Joconde";
		String body2 = "This is the second body of the Joconde";
		joconde.addTextField("body1", body1);
		joconde.addTextField("body2", body2);
		joconde.setAuthor(pastis);
		
		resourceDao.create("/", joconde);


		// We expect that the result won't be null and equals to the persisted Content
		joconde = (Content)resourceDao.get("/joconde");
		Assert.assertNotNull("Joconde is null", joconde);
		Assert.assertEquals("content Joconde and its copy should be equals", "Joconde", joconde.getTitle());
		
		joconde.setTitle("Joconde 2");
		resourceDao.update(joconde);
		joconde = (Content)resourceDao.get("/joconde");
	    // We expect that the result won't be null that the body has been modified
        Assert.assertNotNull("Joconde is null", joconde);
		Assert.assertEquals("content Joconde and its copy should be equals", "Joconde 2", joconde.getTitle());
       
		resourceDao.remove(joconde.getPath());
        // We expect that the joconde Content is not present anymore
        joconde = (Content)resourceDao.get("/joconde2");
        Assert.assertNull("content Joconde is not null", joconde);
	}
	
//	@Test
//	@Rollback(false)
//	@Transactional(readOnly=false)
//	public void testFindByType() throws Exception {
//		if(!resourceDao.exists("/catapulte")) {
//			Resource catapulte = new Resource("catapulte");
//			Content mangez = new Content("mangez");
//			Resource du = new Resource("du");
//			Content veau = new Content("veau");
//			catapulte.addChild(mangez);
//			catapulte.addChild(du);
//			catapulte.addChild(veau);
//			resourceDao.create("/", catapulte);
//		}
//		
//		Resource catapulte = (Resource)resourceDao.get("/catapulte");		
//		List<IResource> resources = resourceDao.findByType(catapulte.getPath(), Content.class.getName());
//		Assert.assertEquals(2, resources.size());
//		
//		resources = resourceDao.findByType("", Content.class.getName());
//		Assert.assertEquals(2, resources.size());
//		
//	}

}
