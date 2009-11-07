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

import javax.annotation.Resource;

import org.resthub.core.AbstractJcrTest;
import org.resthub.core.domain.model.Widget;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test cases for WidgetService
 * @author Bouiaw
 */
public class WidgetServiceTest extends AbstractJcrTest {
	
	/**
	 * The WidgetService to test 
	 */
	@Resource  
    private ResourceService widgetService;
	
    /**
     * Logger 
     */
    @SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(WidgetServiceTest.class);

    /**
     * Test read and write of a Widget that contain Page, Component, ComponentInstance and Content
     * @throws Exception 
     */
    @Test
    public void testCreate() throws Exception {
        
        Widget widget1 = new Widget("widget1");
        widget1.setExtension("mxml");
        
        widget1.setCode("<mx:Module xmlns:mx=\"http://www.adobe.com/2006/mxml\" horizontalAlign=\"center\" verticalAlign=\"middle\">" +
        					"<mx:Label text=\"toto\">" +
        					"</mx:Module>"
         );

        widgetService.create(widget1);

        @SuppressWarnings("unused")
		Widget widget1Copy = (Widget)widgetService.retreive("/widget1");
    }
    
	/**
	 * Just run FindAll to check that no exceptions are thrown
	 */
    @Test
    public void testFindAll() throws Exception {
    	widgetService.findAll();
    }

}
