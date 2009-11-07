package org.resthub.core.domain.model;

import org.jcrom.annotations.JcrProperty;

/**
 * Describe how a Resource will be displayed.
 * The Widget will have a resource parameter and will use it to render itself.
 *  
 * @author Bouiaw
 */
public class Widget extends Resource {
	
    /**
     * uid used for serialization
     */
	private static final long serialVersionUID = 7512413453763748321L;
	
	/**
	 * Extension that will identify code type, mxml for example 
	 */
	@JcrProperty
	private String extension;
	
	
	/**
	 * Code that describe how the a Resource will be displayed
	 */
	@JcrProperty
	private String code;
	
	/**
	 * Classname of the resource that will be displayed by this widget
	 * For example org.resthub.core.domain.model.Content or
	 * org.resthub.core.domain.model.Page
	 */
	@JcrProperty
	private	String resourceType;

	/**
     * Create a new widget 
     */
    public Widget() {
    	super();
    }    
    
    public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	/**
     * Create a new widget
     * @param name name of the widget
     */
    public Widget(String name) {
    	super(name);
    }
    
    public Widget(String name, String extension, String code) {
    	super(name);
    	this.extension = extension;
    	this.code = code;
    }

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	

}
