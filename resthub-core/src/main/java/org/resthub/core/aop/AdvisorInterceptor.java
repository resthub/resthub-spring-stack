package org.resthub.core.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.AopUtils;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;

/**
 * Define if a method invocation should be advised depending on the annotation
 * the method (or its class) is holding
 * 
 * @author Nicolas Carlier
 * @author bmeurant <Baptiste Meurant>
 */
@SuppressWarnings("serial")
public abstract class AdvisorInterceptor extends
        StaticMethodMatcherPointcutAdvisor implements MethodInterceptor {

    /**
     * Annotation type that the method (or its class) should hold to be advised
     */
    protected Class<? extends Annotation> annotationType;

    /**
     * Initialize advice
     */
    public AdvisorInterceptor() {
        super();
        setAdvice(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract Object invoke(MethodInvocation method) throws Throwable;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean matches(Method method, Class<?> targetClass) {

        if (matchFromMethod(method, targetClass)) {
            return true;
        } else {
            return matchFromClass(targetClass);
        }
    }

    /**
     * @param method
     *            method to check
     * @param targetClass
     *            target class that was used to perform method call
     * @return true if the method or any of its its parents methods (if
     *         annotationType allows it) is holding the target annotation, false
     *         otherwise.
     */
    private boolean matchFromMethod(Method method, Class<?> targetClass) {
        Collection<Annotation> annotations;
        Method specificMethod = AopUtils.getMostSpecificMethod(method,
                targetClass);
        annotations = Arrays.asList(specificMethod.getAnnotations());

        if ((annotations == null) || (annotations.isEmpty())) {
            return false;
        }

        return isAnnotationPresent(annotations);
    }

    /**
     * @param targetClass
     *            target class that was used to perform method call
     * @return true if the target class or any of its parents (if annotationType
     *         allows it) is holding the target annotation, false otherwise
     */
    private boolean matchFromClass(Class<?> targetClass) {

        Collection<Annotation> annotations = Arrays.asList(targetClass
                .getAnnotations());
        if (isAnnotationPresent(annotations)) {
            return true;
        }
        return false;

    }

    /**
     * @param annotations
     *            annotations collections to check
     * @return true if the annotation type (defined at instance level) is
     *         present in the annotations collection, false otherwise
     */
    private boolean isAnnotationPresent(Collection<Annotation> annotations) {
        if ((annotations == null) || (annotations.isEmpty())) {
            return false;
        }
        for (Annotation annot : annotations) {
            if (annotationType.isAssignableFrom(annot.getClass())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Set annotation that the method (or its class) should hold to be advised.
     * Do nothing if the annotation does not exists.
     * 
     * @param annotation
     *            annotation
     */
    @SuppressWarnings("unchecked")
    public void setAnnotation(String annotation) throws ClassNotFoundException {
        annotationType = (Class<? extends Annotation>) Thread.currentThread()
                .getContextClassLoader().loadClass(annotation);
    }
}
