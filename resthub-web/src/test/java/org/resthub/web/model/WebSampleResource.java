package org.resthub.web.model;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlRootElement;

import org.resthub.core.model.Resource;

@Entity
@XmlRootElement
public class WebSampleResource extends Resource {
    private static final long serialVersionUID = -7178337784737750452L;

    private String name;

    public WebSampleResource() {
        super();
    }

    public WebSampleResource(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
