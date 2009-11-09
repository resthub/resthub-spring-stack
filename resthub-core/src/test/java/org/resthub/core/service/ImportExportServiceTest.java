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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.resthub.core.domain.model.test.SampleResource;
import org.resthub.test.AbstractJcrTest;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Test cases for SiteService
 * @author Bouiaw
 */
public class ImportExportServiceTest extends AbstractJcrTest {

	@Autowired
	private ImportExportService importExportService;
	
	@javax.annotation.Resource
    private ResourceService testService;
	
	String path = "./target/testExport.zip";

	/**
	 * Logger 
	 */
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(ImportExportServiceTest.class);
	
	@Test
	public void testExport() throws Exception {
		
		SampleResource newResource = new SampleResource("newResource");
		testService.create(newResource);
	        
		FileOutputStream fos = new FileOutputStream(new File(path));
		importExportService.exportResources(fos);

	}
	
	@Test
	public void testImport() throws Exception {
		FileInputStream fis = new FileInputStream(new File(path));
		importExportService.importResources(fis);
	}
	
}
