package org.resthub.web.validation.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class AModel extends AbstractModel {

    private String field2;
    private BModel field5;
    private String field6;

    @Pattern(regexp = ".*")
    public String getField2() {
        return field2;
    }

    public void setField2(String field2) {
        this.field2 = field2;
    }

    @NotNull
    @Valid
    public BModel getField5() {
        return field5;
    }

    public void setField5(BModel field5) {
        this.field5 = field5;
    }

    public String getField6() {
        return field6;
    }

    public void setField6(String field6) {
        this.field6 = field6;
    }
}
