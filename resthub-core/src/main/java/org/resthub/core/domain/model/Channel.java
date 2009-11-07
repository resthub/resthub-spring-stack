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

/**
 * A channel is a containder intended to organize a contents.
 *  
 * @author Bouiaw
 */

public class Channel extends Resource {

	/**
	 * uid used for serialization
	 */
	private static final long serialVersionUID = 2419450437967956542L;
	
	/**
     * Create a new channel 
     */
    public Channel() {
    	super();
    }    
    
    /**
     * Create a new channel
     * @param name name of the channel
     */
    public Channel(String name) {
    	super(name);
    }
	
	/**
     * @return A String image of the current Component object.
     */
    @Override
    public String toString() {
        return "Channel: [] " + super.toString();
    }
}
