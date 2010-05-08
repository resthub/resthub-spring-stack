package org.resthub.web.domain.model;


import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlRootElement;

import org.resthub.core.domain.model.Resource;

@Entity
@XmlRootElement
public class WebSampleResource extends Resource {

	public WebSampleResource() {
		super();
	}

	public WebSampleResource(String ref) {
		super(ref);
	}

	private static final long serialVersionUID = -7178337784737750452L;
	

}
