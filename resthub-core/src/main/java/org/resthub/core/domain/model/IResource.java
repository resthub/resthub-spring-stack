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

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import org.jcrom.annotations.JcrNode;

/**
 * Commons interface for all resources, that must implements directly (not transitively
 * by an inherited class) in order to be able to use JCROm dynamic instantiation.
 * 
 * More details on http://code.google.com/p/jcrom/wiki/DynamicInstantiation
 * 
 * @author bouiaw
 */
@JcrNode(classNameProperty="className", mixinTypes = {"mix:referenceable"})
public interface IResource extends Serializable {

    public String getPath();
    public void setPath(String path);

    public Calendar getCreationDate();
    public void setCreationDate(Calendar creationDate);
    
    public String getName();
    public void setName(String name);
    
    public String getTitle();
	public void setTitle(String title);
	
	public String getUid();
	public void setUid(String uid);
	
	public List<Resource> getChildren();
	public void setChildren(List<Resource> children);
	
	public Resource getParent();
	public void setParent(Resource parent);
	
	public void addChild(Resource child);


}
