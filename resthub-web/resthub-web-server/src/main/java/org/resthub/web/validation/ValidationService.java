package org.resthub.web.validation;

import java.util.Locale;

/**
 * Provides service methods to export validation constraints configured on a given model object.
 *
 * This interface is validation provider agnostic and could be implemented to support any kind of
 * validation constraint API
 */
public interface ValidationService {

    /**
     * Find validation constraints for a model class name and return their representation
     *
     * @param canonicalClassName complete class name of the model to find
     *
     * @return a {@link org.resthub.web.validation.ModelConstraint} wrapper containing all validation
     * constraints retrieved for the given <tt>canonicalClassName</tt>
     *
     * @throws ClassNotFoundException if <tt>canonicalClassName</tt> does not match any class
     */
    ModelConstraint getConstraintsForClassName(String canonicalClassName) throws ClassNotFoundException;

    /**
     * Find validation constraints for a model class name and return their representation.
     * This method also enhances constraints with concrete resolved constraint error message depending
     * on the given <tt>locale</tt>
     *
     * @param canonicalClassName complete class name of the model to find
     * @param locale {@link java.util.Locale} object used to resolve localized messages, if any
     *
     * @return a {@link org.resthub.web.validation.ModelConstraint} wrapper containing all validation
     * constraints retrieved for the given <tt>canonicalClassName</tt>
     *
     * @throws ClassNotFoundException if <tt>canonicalClassName</tt> does not match any class
     */
    ModelConstraint getConstraintsForClassName(String canonicalClassName, Locale locale) throws ClassNotFoundException;

    /**
     * Find validation constraints for a model class and return their representation
     *
     * @param clazz class of the model to find
     *
     * @return a {@link org.resthub.web.validation.ModelConstraint} wrapper containing all validation
     * constraints retrieved for the given <tt>clazz</tt>
     */
    ModelConstraint getConstraintsForClass(Class<?> clazz);

    /**
     * Find validation constraints for a model class and return their representation.
     * This method also enhances constraints with concrete resolved constraint error message depending
     * on the given <tt>locale</tt>
     *
     * @param clazz class of the model to find
     * @param locale {@link java.util.Locale} object used to resolve localized messages, if any
     *
     * @return a {@link org.resthub.web.validation.ModelConstraint} wrapper containing all validation
     * constraints retrieved for the given <tt>clazz</tt>
     */
    ModelConstraint getConstraintsForClass(Class<?> clazz, Locale locale);
}
