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

import org.jcrom.annotations.JcrReference;

/**
 * A block is used to link a Resource and a Widget in order to be displayed in a page
 *  
 * @author Bouiaw
 */

public class Block extends Resource {

	/**
	 * uid used for serialization
	 */
	private static final long serialVersionUID = -7998989924742781885L;
	
	/**
	 * The resource used to display in this block 
	 */
	@JcrReference(byPath=true)
	private Resource renderedResource;
	
	/**
	 * The widget used to display in this block 
	 */
	@JcrReference(byPath=true)
	private Widget renderedWidget;


	/**
     * Create a new block 
     */
    public Block() {
    	super();
    }    
    
    /**
     * Create a new block
     * @param name name of the block
     */
    public Block(String name) {
    	super(name);
    }
	
	/**
     * @return A String image of the current Component object.
     */
    @Override
    public String toString() {
        return "Block: [] " + super.toString();
    }

	public void setRenderedResource(Resource renderedResource) {
		this.renderedResource = renderedResource;
	}

	public Resource getRenderedResource() {
		return renderedResource;
	}

	public void setRenderedWidget(Widget renderedWidget) {
		this.renderedWidget = renderedWidget;
	}

	public Widget getRenderedWidget() {
		return renderedWidget;
	}
}
