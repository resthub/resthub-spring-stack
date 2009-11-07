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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.resthub.core.AbstractJcrTest;
import org.resthub.core.domain.model.ContentType;
import org.resthub.core.domain.model.Resource;
import org.resthub.core.exception.ResthubException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test cases for ProductService
 * @author pastis
 */
public class ContentTypeServiceTest extends AbstractJcrTest {
	
	/**
	 * The ProductService to test 
	 */
	@javax.annotation.Resource
    private ResourceService contentTypeService;

    /**
     * Logger 
     */
    @SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(ContentTypeServiceTest.class);
    

    /**
     * Test creation of a Product
     */
    @Test
    public void testCreate() {
    	
    	String contentTypeName = "contentTypeName1";
    	
    	// Test if the product does not already exist
    	try {
			assertFalse("Product already exists",contentTypeService.exists(contentTypeName));
		} catch (ResthubException e) {
			fail("Error when calling exists before create");
		}
    	
    	// Initialize new ContentType
    	ContentType contentType = new ContentType(contentTypeName);
    	contentType.setTitle("productTitle1");
    	
    	// create new ContentType
    	try {
    		contentTypeService.create(contentType);
		} catch (ResthubException e) {
			fail("Error when calling create");
		}

    	// Test if the product has been created
    	try {
			assertTrue("Product creation failed",contentTypeService.exists(contentTypeName));
		} catch (ResthubException e) {
			fail("Error when calling exists after create");
		}
    	
    	// calculate expected date
    	Calendar expectedCal = Calendar.getInstance();
    	
    	// Retrieve created Product
    	ContentType contentTypeRetrieved = null;
		try {
			contentTypeRetrieved = (ContentType) contentTypeService.retreive(contentTypeName);
		} catch (ResthubException e) {
			fail("Error when calling retrieve");
		}
		assertNotNull("Created ContentType is null", contentTypeRetrieved);
    	assertEquals("Created ContentType Name does not match expected name",contentType.getName(), contentTypeRetrieved.getName());
		
		// Get retrieved date
    	Calendar retrievedCal = contentTypeRetrieved.getCreationDate();
		assertEquals("Created ContentType Creation Date year does not match expected year",expectedCal.get(Calendar.YEAR), retrievedCal.get(Calendar.YEAR));
		assertEquals("Created ContentType Creation Date month does not match expected month",expectedCal.get(Calendar.MONTH), retrievedCal.get(Calendar.MONTH));
		assertEquals("Created ContentType Creation Date day does not match expected day",expectedCal.get(Calendar.DAY_OF_MONTH), retrievedCal.get(Calendar.DAY_OF_MONTH));
    }
    
    /**
     * Test update of a Product
     */
    @Test
    public void testUpdate() {
    	
    	String contentTypeName = "contentTypeName2";
    	
    	// Initialize new Product
    	ContentType contentType = new ContentType(contentTypeName);
    	contentType.setTitle("contentTypeTitle2");
    	
    	// Test if the product does not already exist
    	try {
			assertFalse("Product already exists",contentTypeService.exists(contentTypeName));
		} catch (ResthubException e) {
			fail("Error when calling exists before create");
		}
    	
    	// create new Product
    	try {
    		contentTypeService.create(contentType);
		} catch (ResthubException e) {
			fail("Error when calling create");
		}
    	
    	// Test if the product has been created
    	try {
			assertTrue("Product creation failed",contentTypeService.exists(contentTypeName));
		} catch (ResthubException e) {
			fail("Error when calling exists after create");
		}

    	// Retrieve created Product
		ContentType contentTypeRetrieved = null;
		try {
			contentTypeRetrieved = (ContentType) contentTypeService.retreive(contentTypeName);
		} catch (ResthubException e) {
			fail("Error when calling retrieve after create");
		}
    	assertNotNull("Created Product is null", contentTypeRetrieved);
    	assertEquals("Created Product Name does not match expected name",contentType.getName(), contentTypeRetrieved.getName());
		
		// Change Product Title
		String newTitle = "contentTypeNewTitle2";
		contentTypeRetrieved.setTitle(newTitle);
		
		// Update Product
		try {
			contentTypeService.update(contentTypeRetrieved);
		} catch (ResthubException e) {
			fail("Error when calling update");
		}
		
		// Retrieve updated Product
		try {
			contentTypeRetrieved = (ContentType) contentTypeService.retreive(contentTypeName);
		} catch (ResthubException e) {
			fail("Error when calling retrieve after update");
		}
		assertNotNull("Updated ContentType is null", contentTypeRetrieved);
    	assertEquals("Updated ContentType Name does not match expected name",contentType.getName(), contentTypeRetrieved.getName());
		assertEquals("Updated ContentType Title does not match expected title",newTitle, contentTypeRetrieved.getTitle());
    }
    
	/**
	 * Just run FindAll to check that no exceptions are thrown
	 */
    @Test
    public void testFindAll() {
    	
    	String firstContentTypeName = "contentTypeName3";
    	
    	// Initialize new Product
    	ContentType firstProduct = new ContentType(firstContentTypeName);
    	
    	// Test if the product does not already exist
    	try {
			assertFalse("Product already exists",contentTypeService.exists(firstContentTypeName));
		} catch (ResthubException e) {
			fail("Error when calling exists before create");
		}
    	
    	// create new Product
    	try {
    		contentTypeService.create(firstProduct);
		} catch (ResthubException e) {
			fail("Error when calling create");
		}
    	
    	// Test if the product has been created
    	try {
			assertTrue("ContentType creation failed",contentTypeService.exists(firstContentTypeName));
		} catch (ResthubException e) {
			fail("Error when calling exists after create");
		}
		
		String secondContentTypeName = "contentTypeName4";
    	
    	// Initialize new Product
		ContentType secondProduct = new ContentType(secondContentTypeName);
    	
    	// Test if the product does not already exist
    	try {
			assertFalse("ContentType already exists",contentTypeService.exists(secondContentTypeName));
		} catch (ResthubException e) {
			fail("Error when calling exists before create");
		}
    	
    	// create new Product
    	try {
    		contentTypeService.create(secondProduct);
		} catch (ResthubException e) {
			fail("Error when calling create");
		}
    	
    	// Test if the product has been created
    	try {
			assertTrue("ContentType creation failed",contentTypeService.exists(secondContentTypeName));
		} catch (ResthubException e) {
			fail("Error when calling exists after create");
		}
    	
		// find all products
		List<Resource> products = new ArrayList <Resource> ();
    	try {
			products = contentTypeService.findAll();
		} catch (ResthubException e) {
			fail("Error when calling findAll");
		}
		
		assertTrue("Products size does not match", products.size() >= 2);
    }
    
    /**
     * Test remove of a Product
     */
    @Test
    public void testRemove() {
    	
    	String contentTypeName = "contentTypeName5";
    	
    	// Test if the product does not already exist
    	try {
			assertFalse("ContentType already exists",contentTypeService.exists(contentTypeName));
		} catch (ResthubException e) {
			fail("Error when calling exists before create");
		}
    	
    	// Initialize new Product
		ContentType contentType = new ContentType(contentTypeName);
    	
    	// create new Product
    	try {
    		contentType = (ContentType)contentTypeService.create(contentType);
		} catch (ResthubException e) {
			fail("Error when calling create");
		}

    	// Test if the product has been created
    	try {
			assertTrue("ContentType creation failed",contentTypeService.exists(contentTypeName));
		} catch (ResthubException e) {
			fail("Error when calling exists after create");
		}
		
		// Test Product remove
		try {
			contentTypeService.remove(contentType);
		} catch (ResthubException e) {
			fail("Error when calling remove");
		}
		
		try {
			assertFalse("ContentType was not correctly removed",contentTypeService.exists(contentTypeName));
		} catch (ResthubException e) {
			fail("Error when calling exists after remove");
		}
    }

}
