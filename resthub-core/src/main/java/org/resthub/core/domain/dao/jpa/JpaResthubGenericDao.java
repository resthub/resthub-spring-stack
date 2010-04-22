package org.resthub.core.domain.dao.jpa;

import java.io.Serializable;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.resthub.core.domain.dao.AbstractGenericDao;
import org.synyx.hades.dao.orm.GenericJpaDao;

public class JpaResthubGenericDao<T, PK extends Serializable> extends
		GenericJpaDao<T, PK> implements AbstractGenericDao<T, PK> {
	
    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(PK id) {
        this.delete(this.readByPrimaryKey(id));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
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
