package org.resthub.web.validation;

import javax.validation.MessageInterpolator;
import javax.validation.metadata.ConstraintDescriptor;

public class ValidationContext implements MessageInterpolator.Context {

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

}
