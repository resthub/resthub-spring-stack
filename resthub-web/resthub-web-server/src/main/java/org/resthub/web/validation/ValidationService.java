package org.resthub.web.validation;

import java.util.Locale;

public interface ValidationService {

    ModelConstraint getConstraintsForClassName(String canonicalClassName) throws ClassNotFoundException;
    ModelConstraint getConstraintsForClassName(String canonicalClassName, Locale locale) throws ClassNotFoundException;

    ModelConstraint getConstraintsForClass(Class<?> clazz);
    ModelConstraint getConstraintsForClass(Class<?> clazz, Locale locale);
}
