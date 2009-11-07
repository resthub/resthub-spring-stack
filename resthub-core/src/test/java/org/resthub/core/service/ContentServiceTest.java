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
import org.resthub.core.domain.model.Channel;
import org.resthub.core.domain.model.Content;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test cases for SiteService
 * @author Bouiaw
 */
public class ContentServiceTest extends AbstractJcrTest {
	
	/**
	 * The SiteService to test 
	 */
	@Resource
    private ResourceService contentService;

    /**
     * Logger 
     */
    @SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(ContentServiceTest.class);
    

    /**
     * Test read and write of a Site that contain Page, Component, ComponentInstance and Content
     * @throws Exception 
     */
    @Test
    public void testCreate() throws Exception {
       
        Channel channel1 = new Channel("channel1");
        Content content1 = new Content("content1");
        channel1.addChild(content1);
        
        

        contentService.create(channel1);

        Channel channel1Copy = (Channel)contentService.retreive("channel1");
        assertEquals("channel1", channel1Copy.getName());
    }
    
}
