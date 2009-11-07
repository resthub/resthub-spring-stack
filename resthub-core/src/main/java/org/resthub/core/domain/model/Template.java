package org.resthub.core.domain.model;

import java.util.List;

import org.jcrom.annotations.JcrProperty;

/**
 * A template defines how a page will be rendered.
 * 
 * @author bouiaw
 */
public class Template extends Widget {

	private static final long serialVersionUID = 4173972923418418950L;

	/**
	 * Blocks must match to module ids defined in the template code. They will define where
	 * each widget will be displayed. 
	 */
	@JcrProperty
	private List<String> blocks;
	
	public List<String> getBlocks() {
		return blocks;
	}

	public void setBlocks(List<String> blocks) {
		this.blocks = blocks;
	}

	/**
     * Create a new template 
     */
    public Template() {
    	super();
    }    
    
    /**
     * Create a new template
     * @param name name of the template
     */
    public Template(String name) {
    	super(name);
    }
}
