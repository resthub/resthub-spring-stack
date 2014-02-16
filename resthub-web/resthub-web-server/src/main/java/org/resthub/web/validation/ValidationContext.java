package org.resthub.web.validation;

import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;

import javax.validation.MessageInterpolator;
import javax.validation.metadata.ConstraintDescriptor;

public class ValidationContext implements MessageInterpolator.Context {

    private static final Log log = LoggerFactory.make();

    private final ConstraintDescriptor<?> constraintDescriptor;
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

    @Override
    public <T> T unwrap(Class<T> type) {
        if (type.isAssignableFrom(ValidationContext.class)) {
            return type.cast(this);
        }
        throw log.getTypeNotSupportedForUnwrappingException( type );
    }

}
