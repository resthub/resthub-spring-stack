package org.resthub.core.domain.dao.jpa;

import java.io.Serializable;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.resthub.core.domain.dao.GenericDao;
import org.resthub.core.util.ClassUtils;

/**
 * JPA implementation of our Generic Dao that can manage any kind of entities.
 * Based on Hades Generic Dao. 
 */
public abstract class GenericJpaDao<T, PK extends Serializable> extends
org.synyx.hades.dao.orm.GenericJpaDao<T, PK> implements GenericDao<T, PK> {
    
    @SuppressWarnings("unchecked")
	public GenericJpaDao() {
    	this.setDomainClass((Class<T>)ClassUtils.getGenericType(this.getClass()));
    }

    public void delete(PK id) {
        this.delete(this.readByPrimaryKey(id));
    }
    
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
