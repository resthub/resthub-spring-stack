package org.resthub.core.domain.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.resthub.core.AbstractResourceClassAware;
import org.resthub.core.domain.dao.ResourceDao;
import org.resthub.core.domain.model.Resource;

/**
 * Generic DAO JPA implementation.
 * @param <T> Resource class
 * @author Loïc Frering <loic.frering@atosorigin.com>
 */
public abstract class AbstractJpaResourceDao<T extends Resource> extends AbstractResourceClassAware<T> implements ResourceDao<T> {

    /** Entity manager */
    private EntityManager em;

    /**
     * Defalut constructor.
     */
    public AbstractJpaResourceDao() {
        super();
    }

    /**
     * Allows inherited classes to override and specifiy another
     * entityManagerFactory in the @PersistenceContext annotation.
     *
     * @param em The injected EntityManager.
     */
    @PersistenceContext
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long count() {
        final Query query = this.em.createQuery("select count(*) from " + this.resourceClass.getName());
        return (Long) query.getSingleResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(T resource) {
        this.em.remove(resource);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) {
        this.em.remove(this.findById(id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<T> findAll(Integer offset, Integer limit) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(resourceClass);
        query.from(resourceClass);

        return em.createQuery(query)
                .setFirstResult(offset * limit)
                .setMaxResults(limit)
                .getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T findById(Long id) {
        return (T) this.em.find(this.resourceClass, id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void flush() {
        this.em.flush();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T save(T resource) {
        return this.em.merge(resource);
    }
}
