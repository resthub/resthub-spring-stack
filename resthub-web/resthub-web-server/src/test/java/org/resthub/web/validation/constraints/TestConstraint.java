package org.resthub.web.validation.constraints;

import org.resthub.web.validation.constraints.validators.TestConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = TestConstraintValidator.class)
@Documented
public @interface TestConstraint {

    String message() default "{org.resthub.test.constraints.LessThan}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
