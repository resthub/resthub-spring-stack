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
 * 
 * @author Nicolas Carlier
 */
@SuppressWarnings("serial")
public abstract class AdvisorInterceptor extends
		StaticMethodMatcherPointcutAdvisor implements MethodInterceptor {

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

		boolean matchFromMethod = matchFromMethod(method, targetClass);
		if (matchFromMethod) {
			return true;
		} else {
			return matchFromClass(targetClass, 1);
		}
	}

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

	private boolean matchFromClass(Class<?> targetClass, Integer level) {

		Collection<Annotation> annotations = Arrays.asList(targetClass
				.getAnnotations());
		if (isAnnotationPresent(annotations)) {
			return true;
		}

		// if (targetClass.getSuperclass() != null) {
		// this.matchFromClass(targetClass.getSuperclass(), ++level);
		// }

		return checkFromSuperClasses(targetClass);

	}

	private boolean checkFromSuperClasses(Class<?> targetClass) {
		Collection<Annotation> annotations;
		Class<?> superClass = targetClass.getSuperclass();
		while (superClass != null) {
			annotations = Arrays.asList(superClass.getAnnotations());
			if (isAnnotationPresent(annotations)) {
				return true;
			}
			superClass = superClass.getSuperclass();
		}
		return false;
	}

	private boolean isAnnotationPresent(Collection<Annotation> annotations) {
		if ((annotations == null) || (annotations.isEmpty())) {
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
	 * 
	 * @param annotation
	 *            annotation
	 */
	@SuppressWarnings("unchecked")
	public void setAnnotation(String annotation) {
		try {
			annotationType = (Class<? extends Annotation>) Thread
					.currentThread().getContextClassLoader().loadClass(
							annotation);
		} catch (ClassNotFoundException e) {
			throw new NullPointerException();
		}
	}
}
