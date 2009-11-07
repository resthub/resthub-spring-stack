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

package org.resthub.core.domain.model;

import org.jcrom.annotations.JcrProperty;
import org.jcrom.annotations.JcrReference;


/**
 * The public website wich will be visited.
 * A site usually match with an url like http://www.mysite.com
 * 
 * @author Bouiaw 
 */
public class Site extends Resource {
	
    /**
     * uid used for serialization
     */
	private static final long serialVersionUID = -935599063008740831L;
	
	/**
	 * Define the page that will be displayed when asking the site url 
	 */
	@JcrReference(byPath=true)
	private Page homepage;
	
	/**
	 * The url of the site like http://www.mysite.com
	 */
	@JcrProperty
	private String url;
	/**
     * Create a new site 
     */
    public Site() {
    	super();
    }    
    
    /**
     * Create a new site
     * @param name name of the folder
     */
    public Site(String name) {
    	super(name);
    }
	
    /**
     * @return a String image of the current Site object.
     */
    @Override
    public String toString() {
        return "Site: " + super.toString();
    }

	public void setHomepage(Page homepage) {
		this.homepage = homepage;
	}

	public Page getHomepage() {
		return homepage;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}


	
}