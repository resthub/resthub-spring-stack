package org.resthub.web.validation.model;

import javax.validation.constraints.Size;

public class BModel extends TopModel {

    private String field4;

    @Size(min=1, max= 10)
    public String getField4() {
        return field4;
    }

    public void setField4(String field4) {
        this.field4 = field4;
    }
}
