package org.resthub.common.view;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ResponseView {
    public Class<?> value();
}
