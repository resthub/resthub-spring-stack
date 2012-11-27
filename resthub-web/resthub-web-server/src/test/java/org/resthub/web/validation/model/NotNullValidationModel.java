package org.resthub.web.validation.model;

import javax.validation.constraints.NotNull;

public class NotNullValidationModel {

    private String field;

    @NotNull
    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
