package org.resthub.core.domain.model.test;

import org.jcrom.annotations.JcrProperty;
import org.resthub.core.domain.model.Resource;

public class SampleResource extends Resource {
	
	private static final long serialVersionUID = -6914779214967076499L;
	
	@JcrProperty
    private String testProperty;
	
    public SampleResource() {
        super();
    }
    
    public SampleResource(String name) {
        super(name);
    }

    
    public void setTestProperty(String testProperty) {
        this.testProperty = testProperty;
	}
	
	public String getTestProperty() {
	        return this.testProperty;
	}


}
