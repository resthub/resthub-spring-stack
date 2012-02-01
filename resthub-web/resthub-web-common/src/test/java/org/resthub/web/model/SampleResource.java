package org.resthub.web.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Model class intended to test SerializationHelper
 */

@XmlRootElement
public class SampleResource {
    private Long id;
    private String name;
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
