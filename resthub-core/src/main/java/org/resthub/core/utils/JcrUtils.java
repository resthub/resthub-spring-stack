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

/**
 * Helper class that provide common used function related to the JCR
 * @author Bouiaw
 */
public final class JcrUtils {
	
	/**
	 * Private default constructor, to prevent instantiation 
	 */
	private JcrUtils() {
		
	}
	/**
	 * Create a new normalized path from an absolute path
	 * @return the normalized path
	 */
	public static String createPath(String path) {
		if (path.endsWith("/")) {
			path = path.substring(0, path.length() - 1);
		}

		if (!path.startsWith("/")) {
			path = "/" + path;
		}
		
		return path;
	}

	/**
	 * Create a new normalized path from two other
	 * @return the normalized path
	 */

	public static String createPath(String root, String path) {
		return createPath(root) + createPath(path);
	}
	
    /**
     * Format and standardize the path of the child to be coherent with the parent path
     * @param parentPath : parent path
     * @param childPath : child path
     * @return the modified child path
     */
    public static String formatChildPath(String parentPath, String childPath) {
        if (null == parentPath) {
            throw new IllegalArgumentException("folder path cannot be null !!");
        }
        if (null == childPath) {
            throw new IllegalArgumentException("child path cannot be null !!");
        }
        // We suppress relative parent path in child path
        String newPath = childPath.replace(parentPath, "");
        // if newPath starts with '/', we suppress it.
        if (newPath.startsWith("/")) {
            newPath.replaceFirst("/", "");
        }
        return newPath;
    }
    
    /**
     * Convert a short path where you can omit children nodes to real resource node.
     * For example, it wil convert /louvre/accueil/autre_page_1 to
     * louvre/children/accueil/children/autre_page_1
     * @param sitePath the path that can omit children nodes
     * @return the real resource path
     */
    public static String convertShortPath(String sitePath) {
    	String path = sitePath;
    	
    	// We skip first slash
    	if((path.length()>0) && (path.substring(0, 1).equals("/"))) {
    		path = path.substring(1, path.length());
    	}
    	
    	// We skip last slash
    	if((path.length()>0) && (path.substring(path.length() - 1, path.length()).equals("/"))) {
    		path = path.substring(0, path.length() -1 );
    	}
    	    	
    	return "/" + path.replaceAll("/", "/children/");
    }
}
