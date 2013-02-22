package org.resthub.web.validation.model;

import org.resthub.web.validation.constraints.TestConstraint;

@TestConstraint
public class ClassLevelConstraintModel {

    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
