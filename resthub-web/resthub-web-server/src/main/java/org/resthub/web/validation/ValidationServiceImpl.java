package org.resthub.web.validation;

import org.springframework.context.annotation.Profile;

import javax.inject.Named;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.PropertyDescriptor;
import java.lang.reflect.Modifier;
import java.util.*;

@Profile("resthub-validation")
@Named("validationService")
public class ValidationServiceImpl implements ValidationService {

    private static final ValidatorFactory FACTORY = Validation.buildDefaultValidatorFactory();
    private static final Validator VALIDATOR = FACTORY.getValidator();

    @Override
    public ModelConstraint getConstraintsForClassName(String canonicalClassName) throws ClassNotFoundException {
        return this.getConstraintsForClassName(canonicalClassName, null, false);
    }

    @Override
    public ModelConstraint getConstraintsForClassName(String canonicalClassName, Boolean keyOnly) throws ClassNotFoundException {
        return this.getConstraintsForClassName(canonicalClassName, null, keyOnly);
    }

    @Override
    public ModelConstraint getConstraintsForClassName(String canonicalClassName, Locale locale) throws ClassNotFoundException {
        return this.getConstraintsForClassName(canonicalClassName, locale, false);
    }

    private ModelConstraint getConstraintsForClassName(String canonicalClassName, Locale locale, Boolean keyOnly) throws ClassNotFoundException {
        return this.getConstraintsForClass(Class.forName(canonicalClassName), locale, keyOnly);
    }

    @Override
    public ModelConstraint getConstraintsForClass(Class<?> clazz) {
        return this.getConstraintsForClass(clazz, null, false);
    }

    @Override
    public ModelConstraint getConstraintsForClass(Class<?> clazz, Locale locale) {
        return this.getConstraintsForClass(clazz, locale, false);
    }

    @Override
    public ModelConstraint getConstraintsForClass(Class<?> clazz, Boolean keyOnly) {
        return this.getConstraintsForClass(clazz, null, keyOnly);
    }

    private ModelConstraint getConstraintsForClass(Class<?> clazz, Locale locale, Boolean keyOnly) {
        ModelConstraint modelConstraint = null;
        BeanDescriptor bd = VALIDATOR.getConstraintsForClass(clazz);

        if (bd.isBeanConstrained() && !Modifier.isAbstract(clazz.getModifiers())) {
            modelConstraint = new ModelConstraint(clazz.getCanonicalName());
            modelConstraint.setConstraints(this.getConstraints(bd, locale, keyOnly));
        }

        return modelConstraint;
    }

    private Map<String, List<ValidationConstraint>> getConstraints(BeanDescriptor bd, Locale locale, Boolean keyOnly) {
        Map<String, List<ValidationConstraint>> constraints = new HashMap<String, List<ValidationConstraint>>();

        for (PropertyDescriptor pd : bd.getConstrainedProperties()) {
            if (pd.hasConstraints() || pd.isCascaded()) {
                constraints.put(pd.getPropertyName(), this.getValidationConstraints(pd, locale, keyOnly));
            }
        }

        return constraints;
    }

    private List<ValidationConstraint> getValidationConstraints(PropertyDescriptor pd, Locale locale, Boolean keyOnly) {
        List<ValidationConstraint> validationConstraints = new ArrayList<ValidationConstraint>();
        String type;

        for (ConstraintDescriptor cd : pd.getConstraintDescriptors()) {
            ValidationConstraint validationConstraint = new ValidationConstraint();

            validationConstraint.setType(this.getType(cd));
            validationConstraint.setMessage(this.getMessage(cd, locale, keyOnly));
            validationConstraint.setAttributes(this.getAttributes(cd));

            validationConstraints.add(validationConstraint);
        }

        if (pd.isCascaded()) {
            ValidationConstraint validationConstraint = new ValidationConstraint();
            validationConstraint.setType("Valid");
            validationConstraint.addAttribute("model", pd.getElementClass().getCanonicalName());
            validationConstraints.add(validationConstraint);
        }

        return validationConstraints;
    }

    private String getType(ConstraintDescriptor cd) {
        String type = cd.getAnnotation().annotationType().toString();
        return type.substring(type.lastIndexOf('.') + 1, type.length());
    }

    private String getMessage(ConstraintDescriptor cd, Locale locale, Boolean keyOnly) {

        if (keyOnly) return cd.getAttributes().get("message").toString();

        String msg;
        String msgKey = cd.getAttributes().get("message").toString();
        ValidationContext validationContext = new ValidationContext(cd, null);

        if (null == locale) {
            msg = FACTORY.getMessageInterpolator().interpolate(msgKey, validationContext);
        } else {
            msg = FACTORY.getMessageInterpolator().interpolate(msgKey, validationContext, locale);
        }

        return msg;
    }

    private Map<String, Object> getAttributes(ConstraintDescriptor cd) {
        Map<String, Object> attributes = new HashMap<String, Object>(cd.getAttributes());

        attributes.remove("payload");
        attributes.remove("groups");
        attributes.remove("message");

        return attributes;
    }
}
