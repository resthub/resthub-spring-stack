package org.resthub.web.validation.model;

import javax.validation.constraints.Size;

public class InheritedClassLevelConstraintModel extends ClassLevelConstraintModel {

    private String description;

    @Size(min=1, max= 10)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
