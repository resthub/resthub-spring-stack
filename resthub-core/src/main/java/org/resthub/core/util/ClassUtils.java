/*
 * Copyright 2008-2010 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.resthub.core.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.resthub.core.domain.dao.GenericDao;
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


    /**
     * Returns the domain class the given class is declared for. Will introspect
     * the given class for extensions of {@link GenericDao} or
     * {@link ExtendedGenericDao} and retrieve the domain class type from its
     * generics declaration.
     * 
     * @param clazz
     * @return the domain class the given class is DAO for or {@code null} if
     *         none found.
     */
    public static Class<?> getGenericType(Class<?> clazz) {

        return getGenericType(clazz, 0);
    }

    /**
     * Returns the generic type with the given index from the given
     * {@link Class} if it implements {@link GenericDao} or
     * {@link ExtendedGenericDao}.
     * 
     * @param clazz
     * @param index
     * @return the domain class for index 0, the id class for index 1.
     */
    public static Class<?> getGenericType(Class<?> clazz, int index) {
		
		Type genericSuperclass = clazz.getGenericSuperclass();

		while (!(genericSuperclass instanceof ParameterizedType)) {
			clazz = clazz.getSuperclass();
			genericSuperclass = clazz.getGenericSuperclass();
		}
		return (Class<?>) ((ParameterizedType) genericSuperclass).getActualTypeArguments()[index];
		

	}

}
