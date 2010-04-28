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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.resthub.core.domain.Page;
import org.resthub.core.domain.PageImpl;
import org.resthub.core.domain.Pageable;
import org.resthub.core.domain.Sort;
import org.resthub.core.domain.dao.GenericDao;
import org.resthub.core.domain.dao.QueryUtils;
import org.resthub.core.util.ClassUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;


/**
 * Default implementation of the {@link GenericDao} interface. This will offer
 * you a more sophisticated interface than the plain {@link EntityManager}.
 * 
 * @author Oliver Gierke - gierke@synyx.de
 * @author Eberhard Wolff
 * @param <T> the type of the entity to handle
 * @param <PK> the type of the entity's identifier
 */
public abstract class GenericJpaDao<T, PK extends Serializable> extends
        GenericDaoSupport<T> implements GenericDao<T, PK> {
    
    public GenericJpaDao() {
    	this.setDomainClass((Class<T>)ClassUtils.getDomainClass(this.getClass()));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.synyx.jpa.support.GenericDao#delete(java.lang.Object)
     */
    public void delete(final T entity) {

        getEntityManager().remove(entity);
    }


    /*
     * (non-Javadoc)
     * 
     * @see org.synyx.hades.dao.GenericDao#delete(java.util.List)
     */
    public void delete(final List<T> entities) {

        if (null == entities || entities.isEmpty()) {
            return;
        }

        QueryUtils.applyAndBind(getDeleteAllQueryString(), entities,
                getEntityManager()).executeUpdate();
    }


    /*
     * (non-Javadoc)
     * 
     * @see org.synyx.hades.dao.GenericDao#deleteAll()
     */
    public void deleteAll() {

        getEntityManager().createQuery(getDeleteAllQueryString())
                .executeUpdate();
    }


    /*
     * (non-Javadoc)
     * 
     * @see
     * com.synyx.jpa.support.GenericDao#readByPrimaryKey(java.io.Serializable)
     */
    public T readByPrimaryKey(final PK primaryKey) {

        Assert.notNull(primaryKey, "The given primaryKey must not be null!");

        return getEntityManager().find(getDomainClass(), primaryKey);
    }


    /*
     * (non-Javadoc)
     * 
     * @see org.synyx.hades.dao.GenericDao#exists(java.io.Serializable)
     */
    public boolean exists(final PK primaryKey) {

        Assert.notNull(primaryKey, "The given primary key must not be null!");

        return null != readByPrimaryKey(primaryKey);
    }


    /*
     * (non-Javadoc)
     * 
     * @see com.synyx.jpa.support.GenericDao#readAll()
     */
    @SuppressWarnings("unchecked")
    public List<T> readAll() {

        return getReadAllQuery().getResultList();
    }


    /*
     * (non-Javadoc)
     * 
     * @see org.synyx.hades.dao.GenericDao#readAll(org.synyx.hades.domain.Sort)
     */
    @SuppressWarnings("unchecked")
    public List<T> readAll(final Sort sort) {

        String queryString =
                QueryUtils.applySorting(getReadAllQueryString(), sort);
        Query query = getEntityManager().createQuery(queryString);

        return (null == sort) ? readAll() : query.getResultList();
    }


    /*
     * (non-Javadoc)
     * 
     * @see
     * org.synyx.hades.hades.dao.GenericDao#readAll(org.synyx.hades.hades.dao
     * .Pageable)
     */
    public Page<T> readAll(final Pageable pageable) {

        if (null == pageable) {

            return new PageImpl<T>(readAll());
        }

        return readPage(pageable, getReadAllQueryString());
    }


    /*
     * (non-Javadoc)
     * 
     * @see org.synyx.hades.hades.jpa.support.GenericDao#count()
     */
    public Long count() {

        return (Long) getEntityManager().createQuery(getCountQueryString())
                .getSingleResult();
    }


    /*
     * (non-Javadoc)
     * 
     * @see com.synyx.jpa.support.GenericDao#save(java.lang.Object)
     */
    public T save(final T entity) {

        if (getIsNewStrategy().isNew(entity)) {
            getEntityManager().persist(entity);
            return entity;
        } else {
            return getEntityManager().merge(entity);
        }
    }


    /*
     * (non-Javadoc)
     * 
     * @see
     * org.synyx.hades.hades.jpa.support.GenericDao#saveAndFlush(org.synyx.hades
     * .hades.jpa.support.Entity)
     */
    public T saveAndFlush(final T entity) {

        T result = save(entity);
        flush();

        return result;
    }


    /*
     * (non-Javadoc)
     * 
     * @see org.synyx.hades.dao.GenericDao#saveAll(java.util.List)
     */
    public List<T> save(List<T> entities) {

        List<T> result = new ArrayList<T>();

        for (T entity : entities) {
            result.add(save(entity));
        }

        return result;
    }


    /*
     * (non-Javadoc)
     * 
     * @see org.synyx.hades.hades.jpa.support.GenericDao#flush()
     */
    public void flush() {

        getEntityManager().flush();
    }


    /**
     * Reads a page of entities for the given JPQL query.
     * 
     * @param pageable
     * @param query
     * @return a page of entities for the given JPQL query
     */
    @SuppressWarnings("unchecked")
    protected Page<T> readPage(final Pageable pageable, final String query) {

        String queryString = QueryUtils.applySorting(query, pageable.getSort());
        Query jpaQuery = getEntityManager().createQuery(queryString);

        jpaQuery.setFirstResult(pageable.getFirstItem());
        jpaQuery.setMaxResults(pageable.getPageSize());

        return new PageImpl<T>(jpaQuery.getResultList(), pageable, count());
    }
    
    public void delete(PK id) {
        this.delete(this.readByPrimaryKey(id));
    }
    
    /**
     * {@inheritDoc}
     */
    public List<T> readAll(Integer offset, Integer limit) {
        CriteriaBuilder cb = this.getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(this.getDomainClass());
        query.from(this.getDomainClass());

        return this.getEntityManager().createQuery(query)
                .setFirstResult(offset * limit)
                .setMaxResults(limit)
                .getResultList();
    }

}
