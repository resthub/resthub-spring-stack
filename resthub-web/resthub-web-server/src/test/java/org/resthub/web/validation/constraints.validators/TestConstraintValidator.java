package org.resthub.web.validation.constraints.validators;

import org.resthub.web.validation.constraints.TestConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TestConstraintValidator implements ConstraintValidator<TestConstraint, Object> {

    private TestConstraint constraint;

    public void initialize(TestConstraint constraintAnnotation) {
        constraint = constraintAnnotation;
    }

    public boolean isValid(Object object, ConstraintValidatorContext cvc) {
        return false;
    }
}