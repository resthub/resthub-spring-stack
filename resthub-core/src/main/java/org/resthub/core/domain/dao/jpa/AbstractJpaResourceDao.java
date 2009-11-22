package org.resthub.core.domain.dao.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.resthub.core.domain.dao.ResourceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Generic DAO implementation for {@link ResourceDao} with JPA.
 * 
 * @author Bouiaw
 */
@SuppressWarnings("unused")
@Transactional
public abstract class AbstractJpaResourceDao<T> implements ResourceDao<T> {

	private final Class<T> entityClass;
	
	private final Logger log;

	private EntityManager entityManager;
	
	/**
	 * 
	 * @param entityClass the class handled by this DAO implementation
	 */
	public AbstractJpaResourceDao( Class<T> entityClass ) {
		this.entityClass = entityClass;
		this.log = LoggerFactory.getLogger(entityClass);
	}
	
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void persist(T transientResource) {
		log.debug("persisting Resource instance");
		try {
			entityManager.persist(transientResource);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
		}
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void persistAndFlush(T transientResource) {
		this.persist(transientResource);
		entityManager.flush();
		entityManager.clear();
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void remove(T persistentResource) {
		log.debug("removing Resource instance");
		try {
			entityManager.remove(persistentResource);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void remove(Long resourceId) {
		this.remove(this.findById(resourceId));
	}

	/**
	 * {@inheritDoc}
	 */
	public T merge(T detachedResource) {
		log.debug("merging Resource instance");
		try {
			T result = entityManager.merge(detachedResource);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public T findById(Long id) {
		log.debug("getting Resource instance with id: " + id);
		try {
			T instance = entityManager.find(entityClass, id);
			log.debug("findById successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("findById failed", re);
			return null;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public T findByPath(String path) {
		log.debug("getting Resource instance with path: " + path);
		try {
			Query query = entityManager.createQuery("select r from " + entityClass.getSimpleName() + " r where r.name like :path");
			query.setParameter("path", path);
			T resource = (T) query.getSingleResult();
			log.debug("findByPath successful");
			return resource;
		} catch (RuntimeException re) {
			log.error("findByPath failed", re);
			return null;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		log.debug("getting all Resource instances");
		try {
			Query query = entityManager.createQuery("select r from " + entityClass.getSimpleName() + " r");
			query.setParameter("class", entityClass.getSimpleName());
			List<T> resourceList = (List<T>) query.getResultList();
			log.debug("findAll successful");
			return resourceList;
		} catch (RuntimeException re) {
			log.error("findAll failed", re);
			return new ArrayList<T>();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public List<T> search(String searchString) {
		log.debug("Search Resource instances with search string: " + searchString);
		try {
			Query query = entityManager.createQuery("select r from " + entityClass.getSimpleName() + " r where r.name like :searchString");
			query.setParameter("class", entityClass.getSimpleName());
			query.setParameter("searchString", searchString);
			List<T> resourceList = (List<T>) query.getResultList();
			log.debug("search successful");
			return resourceList;
		} catch (RuntimeException re) {
			log.error("search failed", re);
			return new ArrayList<T>();
		}
	}
	
	

}
