package org.resthub.core.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.springframework.aop.support.AopUtils;

/**
 * Utility class to work with classes.
 */
public abstract class ClassUtils {

	/**
	 * Private constructor to prevent instantiation.
	 */
	private ClassUtils() {

	}

	public static Class<?> getGenericTypeFromBean(Object object) {
		Class<?> clazz = object.getClass();

		if (AopUtils.isAopProxy(object)) {
			clazz = AopUtils.getTargetClass(object);
		}
		return getGenericType(clazz);
	}

	public static Class<?> getGenericType(Class<?> clazz) {

		return getGenericType(clazz, 0);
	}

	/**
	 * Returns the generic type with the given index from the given
	 * {@link Class}. Scan all base classes until finding a generic type.
	 * 
	 * @param clazz
	 *            Class where seeking the generic type
	 * @param index
	 * @return the generic type
	 */
	public static Class<?> getGenericType(Class<?> clazz, int index) {

		Type genericSuperclass = clazz.getGenericSuperclass();

		if (genericSuperclass == null) {
			return null;
		}

		Class<?> effectiveClass = clazz;
		while (!(genericSuperclass instanceof ParameterizedType)) {
		    effectiveClass = effectiveClass.getSuperclass();
			genericSuperclass = effectiveClass.getGenericSuperclass();

			if (effectiveClass.equals(Object.class)) {
				return null;
			}
		}
		return (Class<?>) ((ParameterizedType) genericSuperclass)
				.getActualTypeArguments()[index];

	}

}
