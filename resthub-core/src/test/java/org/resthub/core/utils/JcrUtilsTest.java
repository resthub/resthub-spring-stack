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

package org.resthub.core.utils;

import static org.junit.Assert.assertEquals;
import junit.framework.Assert;

import org.junit.Test;

/**
 * Test cases for JcrUtils
 * @author Bouiaw
 */
public class JcrUtilsTest {
	
	
	/**
	 * Test createPath from an empty path
	 */
	@Test
	public void emptyPath() {
		String path = JcrUtils.createPath("");
		assertEquals("/", path);
	}

	/**
	 * Test createPath from an one relative path
	 */
	@Test
	public void oneName() {
		String path = JcrUtils.createPath("toto");
		assertEquals("/toto", path);
		
	}
	
	/**
	 * Test createPath from an one absolute path
	 */
	@Test
	public void onePath() {
		String path = JcrUtils.createPath("/toto");
		assertEquals("/toto", path);
	}
	
	/**
	 * Test createPath from an one absolute path ended by slash
	 */
	@Test
	public void onePathWithEndSlash() {
		String path = JcrUtils.createPath("/toto/");
		assertEquals("/toto", path);
	}
	
	/**
	 * Test createPath from two path  : first absolute and second relative
	 */
	@Test
	public void twoPathsWithPathAndName() {
		String path = JcrUtils.createPath("/toto", "titi");
		assertEquals("/toto/titi", path);
	}
	
	/**
	 * Test createPath from two path  : first absolute and second absolute
	 */
	@Test
	public void twoPathsWithPathAndPath() {
		String path = JcrUtils.createPath("/toto", "/titi");
		assertEquals("/toto/titi", path);
	}
	
	/**
	 * Test createPath from two path  : first relative and second relative
	 */
	@Test
	public void twoPathsWithNameAndName() {
		String path = JcrUtils.createPath("toto", "titi");
		assertEquals("/toto/titi", path);
	}
	
	@Test
	public void shortPath() {
		Assert.assertEquals("/", JcrUtils.convertShortPath("/"));
		Assert.assertEquals("/toto", JcrUtils.convertShortPath("/toto")) ;
		Assert.assertEquals("/louvre/children/accueil/children/autre_page_1", JcrUtils.convertShortPath("/louvre/accueil/autre_page_1")) ;
	}
}
