package org.resthub.core;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.resthub.core.domain.model.Resource;

public abstract class AbstractResourceClassAware<T extends Resource> {

	protected Class<T> resourceClass;

	@SuppressWarnings("unchecked")
	public AbstractResourceClassAware() {
		Class clazz = getClass();
		Type genericSuperclass = clazz.getGenericSuperclass();

		if (genericSuperclass.equals(Object.class)) {
			this.resourceClass = (Class<T>) Resource.class;
		} else {
			while (!(genericSuperclass instanceof ParameterizedType)) {
				clazz = clazz.getSuperclass();
				genericSuperclass = clazz.getGenericSuperclass();
			}
			this.resourceClass = (Class<T>) ((ParameterizedType) genericSuperclass)
					.getActualTypeArguments()[0];
		}

	}

}
