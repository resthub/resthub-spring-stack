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

package org.resthub.core.domain.dao.jpa;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.resthub.core.domain.Persistable;
import static org.resthub.core.domain.dao.QueryUtils.*;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;
import org.springframework.util.ReflectionUtils.MethodCallback;


/**
 * Abstract base class for generic DAOs.
 * 
 * @author Oliver Gierke - gierke@synyx.de
 * @param <T> the type of entity to be handled
 */
public abstract class GenericDaoSupport<T> {

    private EntityManager entityManager;
    private Class<T> domainClass;
    private IsNewStrategy isNewStrategy;


    /**
     * Returns the {@link EntityManager}.
     * 
     * @return
     */
    protected EntityManager getEntityManager() {

        return this.entityManager;
    }


    /**
     * Setter to inject {@code EntityManager}.
     * 
     * @param entityManager
     */
    @PersistenceContext
    public void setEntityManager(final EntityManager entityManager) {

        this.entityManager = entityManager;

        validate();
    }


    /**
     * Returns the domain class to handle.
     * 
     * @return the domain class
     */
    protected Class<T> getDomainClass() {

        return domainClass;
    }


    /**
     * Sets the domain class to handle.
     * 
     * @param domainClass the domain class to set
     */
    public void setDomainClass(final Class<T> domainClass) {

        this.domainClass = domainClass;
        createIsNewStrategy(domainClass);
    }


    /**
     * Returns the query string to retrieve all entities.
     * 
     * @return string to retrieve all entities
     */
    protected String getReadAllQueryString() {

        return getQueryString(READ_ALL_QUERY, getDomainClass());
    }


    /**
     * Returns the query string to delete all entities.
     * 
     * @return string to delete all entities
     */
    protected String getDeleteAllQueryString() {

        return getQueryString(DELETE_ALL_QUERY_STRING, getDomainClass());
    }


    /**
     * Returns the query string to count entities.
     * 
     * @return string to count entities
     */
    protected String getCountQueryString() {

        return getQueryString(COUNT_QUERY_STRING, getDomainClass());
    }


    /**
     * Returns the query to retrieve all entities.
     * 
     * @return the query to retrieve all entities.
     */
    protected Query getReadAllQuery() {

        return getEntityManager().createQuery(getReadAllQueryString());
    }


    /**
     * Asserts that the {@code EntityManager} implementation being used by the
     * dao is an instance of the given type.
     * 
     * @param clazz
     * @throws IllegalArgumentException if the entity manager is not of the
     *             given type
     */
    protected void assertEntityManagerClass(Class<? extends EntityManager> clazz) {

        Assert.isInstanceOf(clazz, entityManager, String.format(
                "%s can only be used with %s implementation! "
                        + "Please check configuration or use %s instead!",
                getClass().getSimpleName(), clazz.getSimpleName(),
                GenericJpaDao.class.getSimpleName()));
    }


    /**
     * Callback method to validate the class setup.
     */
    public void validate() {

        if (null == entityManager) {
            throw new IllegalStateException("EntityManager must not be null!");
        }
    }


    /**
     * Return whether the given entity is to be regarded as new. Default
     * implementation will inspect the given domain class and use either
     * {@link PersistableIsNewStrategy} if the class implements
     * {@link Persistable} or {@link ReflectiveIsNewStrategy} otherwise.
     * 
     * @param entity
     * @return
     */
    protected void createIsNewStrategy(Class<?> domainClass) {

        if (Persistable.class.isAssignableFrom(domainClass)) {
            this.isNewStrategy = new PersistableIsNewStrategy();
        } else {
            this.isNewStrategy = new ReflectiveIsNewStrategy(domainClass);
        }
    }


    /**
     * Returns the strategy how to determine whether an entity is to be regarded
     * as new.
     * 
     * @return the isNewStrategy
     */
    protected IsNewStrategy getIsNewStrategy() {

        return isNewStrategy;
    }

    /**
     * Interface to abstract the ways to determine if a
     * 
     * @author Oliver Gierke
     */
    public interface IsNewStrategy {

        public boolean isNew(Object entity);
    }

    /**
     * Implementation of {@link IsNewStrategy} that assumes the entity handled
     * implements {@link Persistable} and uses {@link Persistable#isNew()} for
     * the {@link #isNew(Object)} check.
     * 
     * @author Oliver Gierke
     */
    public static class PersistableIsNewStrategy implements IsNewStrategy {

        /*
         * (non-Javadoc)
         * 
         * @see
         * org.synyx.hades.dao.orm.GenericJpaDao.IsNewStrategy#isNew(java.lang
         * .Object)
         */
        public boolean isNew(Object entity) {

            return ((Persistable<?>) entity).isNew();
        }
    }

    /**
     * {@link IsNewStrategy} implementation that reflectively checks a
     * {@link Field} or {@link Method} annotated with {@link Id}.
     * 
     * @author Oliver Gierke
     */
    public static class ReflectiveIsNewStrategy implements IsNewStrategy {

        private Field field;
        private Method method;


        /**
         * Creates a new {@link ReflectiveIsNewStrategy} by inspecting the given
         * class for a {@link Field} or {@link Method} for and {@link Id}
         * annotation.
         * 
         * @param domainClass
         */
        public ReflectiveIsNewStrategy(Class<?> domainClass) {

            ReflectionUtils.doWithFields(domainClass, new FieldCallback() {

                public void doWith(Field field)
                        throws IllegalArgumentException, IllegalAccessException {

                    boolean idFieldFound =
                            ReflectiveIsNewStrategy.this.field != null;
                    boolean isIdField = field.getAnnotation(Id.class) != null;

                    if (!idFieldFound && isIdField) {
                        ReflectiveIsNewStrategy.this.field = field;
                    }
                }
            });

            if (field != null) {
                return;
            }

            ReflectionUtils.doWithMethods(domainClass, new MethodCallback() {

                public void doWith(Method method)
                        throws IllegalArgumentException, IllegalAccessException {

                    boolean idMethodFound =
                            ReflectiveIsNewStrategy.this.method != null;
                    boolean isIdMethod =
                            AnnotationUtils.findAnnotation(method, Id.class) != null;

                    if (!idMethodFound && isIdMethod) {
                        ReflectiveIsNewStrategy.this.method = method;
                    }
                }
            });
        }


        /*
         * (non-Javadoc)
         * 
         * @see
         * org.synyx.hades.dao.orm.GenericJpaDao.IsNewStrategy#isNew(java.lang
         * .Object)
         */
        public boolean isNew(Object entity) {

            if (field != null) {
                ReflectionUtils.makeAccessible(field);
                return ReflectionUtils.getField(field, entity) == null;
            }

            ReflectionUtils.makeAccessible(method);
            return ReflectionUtils.invokeMethod(method, entity) == null;
        }
    }
}
