package org.resthub.web.validation.model;

import javax.validation.constraints.NotNull;

public class TopModel {

    private String field3;

    @NotNull
    public String getField3() {
        return field3;
    }

    public void setField3(String field3) {
        this.field3 = field3;
    }
}
