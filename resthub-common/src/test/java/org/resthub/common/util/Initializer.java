package org.resthub.common.util;

import javax.inject.Named;

@Named("initializer")
public class Initializer {
    private Boolean value = false;

    @PostInitialize
    public void init() {
        value = true;
    }

    public Boolean getValue() {
        return value;
    }
}
