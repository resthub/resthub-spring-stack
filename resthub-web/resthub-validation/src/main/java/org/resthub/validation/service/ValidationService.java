package org.resthub.validation.service;

import org.resthub.validation.model.ModelConstraint;

import java.util.Locale;

public interface ValidationService {

    ModelConstraint getConstraintsForClassName(String canonicalClassName) throws ClassNotFoundException;
    ModelConstraint getConstraintsForClass(Class<?> clazz);
    ModelConstraint getConstraintsForClassName(String canonicalClassName, Locale locale) throws ClassNotFoundException;
    ModelConstraint getConstraintsForClass(Class<?> clazz, Locale locale);
}
