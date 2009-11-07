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

import static org.junit.Assert.assertEquals;

import javax.annotation.Resource;

import org.resthub.core.AbstractJcrTest;
import org.resthub.core.domain.model.Page;
import org.resthub.core.domain.model.Site;
import org.resthub.core.domain.model.Template;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test cases for SiteService
 * @author Bouiaw
 */
public class SiteServiceTest extends AbstractJcrTest {
	
	/**
	 * The SiteService to test 
	 */
	@Resource
    private ResourceService siteService;

    /**
     * Logger 
     */
    @SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(SiteServiceTest.class);
    

    /**
     * Test read and write of a Site that contain Page, Component, ComponentInstance and Content
     * @throws Exception 
     */
    @Test
    public void testCreate() throws Exception {
       
        Site louvre = new Site("louvretest");
        Page homepage = new Page("homepage");
        Page childpage1 = new Page("childpage1");
        Page childpage2 = new Page("childpage2");
        @SuppressWarnings("unused")
		Template mainTemplate = new Template("mainTemplate");
        Template homepageTemplate = new Template("homepageTemplate");
        
        homepage.setTemplate(homepageTemplate);
        homepage.addChild(childpage1);
        homepage.addChild(childpage2);
        louvre.setTitle("Louvre");
        louvre.setUrl("http://www.louvre.fr");
        louvre.setHomepage(homepage);
        

        siteService.create(louvre);

        Site louvre2 = (Site)siteService.retreive("/louvretest");
        assertEquals("louvretest", louvre2.getName());
    }
    
	/**
	 * Just run FindAll to check that no exceptions are thrown
	 */
    @Test
    public void testFindAll() throws Exception {
    	siteService.findAll();
    }

}
