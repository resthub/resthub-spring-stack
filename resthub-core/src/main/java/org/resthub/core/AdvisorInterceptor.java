package org.resthub.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.AopUtils;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;

/**
 * Advisor Interceptor.
 * @author Nicolas Carlier
 */
@SuppressWarnings("serial")
public abstract class AdvisorInterceptor extends StaticMethodMatcherPointcutAdvisor implements MethodInterceptor {

	protected Class<? extends Annotation> annotationType;
    protected Annotation annotationTarget;

    /**
     * Default constructor.
     */
    public AdvisorInterceptor() {
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
        Method specificMethod = AopUtils.getMostSpecificMethod(method, targetClass);
        Collection<Annotation> annotations = Arrays.asList(specificMethod.getAnnotations());

        if (annotations == null) {
            return false;
        }

        for (Annotation annot : annotations) {
            if (annotationType.isAssignableFrom(annot.getClass())) {
                annotationTarget = annot;
                return true;
            }
        }
        return false;
    }

    /**
     * Set annotation.
     * @param annotation annotation
     */
    @SuppressWarnings("unchecked")
	public void setAnnotation(String annotation) {
        try {
            annotationType = (Class<? extends Annotation>) Thread.currentThread().
                    getContextClassLoader().loadClass(annotation);
        } catch (ClassNotFoundException e) {
            throw new NullPointerException();
        }
    }
}
