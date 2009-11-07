package org.resthub.core.domain.model;

import java.util.List;
import org.jcrom.annotations.JcrChildNode;
import org.jcrom.annotations.JcrReference;

/**
 * A page match to an url directluy accessible in a browser.  It defines a list of 
 * renderable resources that will be displayed thanks to their widgets.
 *  * 
 * @author bouiaw
 */
public class Page extends Resource {
	
	private static final long serialVersionUID = 8745925246644685093L;
	
	/**
	 * List of blocks to be rendered in this page.
	 */
	@JcrChildNode
	private List<Block> blocks;
	
    /**
     * Template to use to render this Resource  
     */
	@JcrReference(byPath=true)
	private Template template;
	
    /**
     * Create a new page 
     */
    public Page() {
    	super();
    	setTemplate(new Template());
    }    
    
    /**
     * Create a new page 
     */
    public Page(String name) {
    	super(name);
    	setTemplate(new Template());
    }

	/**
	 * @param blocks the blocks to set
	 */
	public void setBlocks(List<Block> blocks) {
		this.blocks = blocks;
	}

	/**
	 * @return the blocks
	 */
	public List<Block> getBlocks() {
		return blocks;
	}

	/**
	 * @param template the template to set
	 */
	public void setTemplate(Template template) {
		this.template = template;
	}

	/**
	 * @return the template
	 */
	public Template getTemplate() {
		return template;
	}

	
	   

}
