package org.resthub.web.validation.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public abstract class AbstractModel {

    private String field1;

    @NotNull
    @Size(min = 1, max = 10)
    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }
}
