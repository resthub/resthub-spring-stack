package org.resthub.web.validation;

import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;

import javax.validation.MessageInterpolator;
import javax.validation.metadata.ConstraintDescriptor;

/**
 * Specific interpolation context management for resthub validation utilities
 *
 * Allows to manage messages and interpolations related to a {@link javax.validation.metadata.ConstraintDescriptor}
 * and a value. Managing the value allows to resolve dynamic message interpolation for error messages
 * containing parameters
 */
public class ValidationContext implements MessageInterpolator.Context {

    private static final Log log = LoggerFactory.make();

    /**
     * Managed constraint descriptor
     */
    private final ConstraintDescriptor<?> constraintDescriptor;

    /**
     * Managed object value
     */
    private final Object validatedValue;

    public ValidationContext(ConstraintDescriptor<?> constraintDescriptor, Object validatedValue) {
        this.constraintDescriptor = constraintDescriptor;
        this.validatedValue = validatedValue;
    }

    public ConstraintDescriptor<?> getConstraintDescriptor() {
        return constraintDescriptor;
    }

    public Object getValidatedValue() {
        return validatedValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T unwrap(Class<T> type) {
        if (type.isAssignableFrom(ValidationContext.class)) {
            return type.cast(this);
        }
        throw log.getTypeNotSupportedForUnwrappingException( type );
    }

}
