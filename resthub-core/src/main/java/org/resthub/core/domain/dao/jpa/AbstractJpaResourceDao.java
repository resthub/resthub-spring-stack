package org.resthub.core.domain.dao.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.resthub.core.AbstractResourceClassAware;
import org.resthub.core.domain.dao.ResourceDao;
import org.resthub.core.domain.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Loïc Frering <loic.frering@atosorigin.com>
 */
public abstract class AbstractJpaResourceDao<T extends Resource> extends
		AbstractResourceClassAware<T> implements ResourceDao<T> {

	private final Logger log;

	@PersistenceContext
	private EntityManager em;

	public AbstractJpaResourceDao() {
		super();
		this.log = LoggerFactory.getLogger(resourceClass);
	}

	public void persist(T transientResource) {
		log.debug("Persisting Resource instance with name: "
				+ transientResource.getName() + ".");
		em.persist(transientResource);
	}

	public T merge(T detachedResource) {
		log.debug("Merging Resource instance with name: "
				+ detachedResource.getName() + ".");
		T result = em.merge(detachedResource);
		return result;
	}

	public void remove(T persistentResource) {
		log.debug("Removing Resource instance with name: "
				+ persistentResource.getName() + ".");
		em.remove(persistentResource);
	}

	public void remove(Long resourceId) {
		this.remove(this.findById(resourceId));
	}

	public void remove(String name) {
		this.remove(this.findByName(name));
	}

	public T findById(Long id) {
		log.debug("Finding Resource instance with id: " + id);
		T instance = em.find(resourceClass, id);
		return instance;
	}

	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		log.debug("Finding all Resource instances.");

		Query query = em.createQuery("select r from "
				+ resourceClass.getSimpleName() + " r");
		List<T> resourceList = query.getResultList();
		return resourceList;
	}

	@SuppressWarnings("unchecked")
	public T findByName(String name) {
		log.debug("Finding Resource instance with name: " + name);
		
		Query query = em.createQuery("select r from "
				+ resourceClass.getSimpleName() + " r where r.name like :name");
		query.setParameter("name", name);
		query.setMaxResults(1);
		List<T> resources = (List<T>) query.getResultList();
		if(resources.size() == 1)
			return resources.get(0);
		else
			return null;
	}

}
