package org.resthub.core.domain.dao.jpa;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.resthub.core.domain.dao.ResourceDao;
import org.resthub.core.domain.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Loïc Frering <loic.frering@atosorigin.com>
 */
@Named("resourceDao")
public class JpaResourceDao<T extends Resource> implements ResourceDao<T> {

    protected Class<T> entityClass;
    private final Logger log;
    
    @PersistenceContext
    private EntityManager em;

    @SuppressWarnings("unchecked")
	public JpaResourceDao() {
        Class clazz = getClass();
        Type genericSuperclass = clazz.getGenericSuperclass();

        if (genericSuperclass.equals(Object.class)) {
            this.entityClass = (Class<T>) Resource.class;
        } else {
            while (!(genericSuperclass instanceof ParameterizedType)) {
                clazz = clazz.getSuperclass();
                genericSuperclass = clazz.getGenericSuperclass();
            }
            this.entityClass = (Class<T>) ((ParameterizedType) genericSuperclass)
                    .getActualTypeArguments()[0];
        }

        this.log = LoggerFactory.getLogger(entityClass);
    }

    public void persist(T transientResource) {
        log.debug("Persisting Resource instance with name: " + transientResource.getName() + ".");
        try {
            em.persist(transientResource);
            log.debug("Persist successfull.");
        } catch (RuntimeException re) {
            log.error("Persist failed.", re);
        }
    }

    public T merge(T detachedResource) {
        log.debug("Merging Resource instance with name: " + detachedResource.getName() + ".");
        try {
            T result = em.merge(detachedResource);
            log.debug("Merge successfull.");
            return result;
        } catch (RuntimeException re) {
            log.error("Merge failed.", re);
            return null;
        }
    }

    public void remove(T persistentResource) {
        log.debug("Removing Resource instance with name: " + persistentResource.getName() + ".");
        try {
            em.remove(persistentResource);
            log.debug("Remove successfull.");
        } catch (RuntimeException re) {
            log.error("Remove failed.", re);
        }
    }

    public void remove(Long resourceId) {
        this.remove(this.findById(resourceId));
    }
    
    public void remove(String name) {
        this.remove(this.findByName(name));
    }

    public T findById(Long id) {
        log.debug("Finding Resource instance with id: " + id);
        try {
            T instance = em.find(entityClass, id);
            log.debug("FindById successfull.");
            return instance;
        } catch (RuntimeException re) {
            log.error("FindById failed.", re);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        log.debug("Finding all Resource instances.");
        try {
            Query query = em.createQuery("select r from " + entityClass.getSimpleName() + " r");
            // query.setParameter("class", entityClass.getSimpleName());
            List<T> resourceList = query.getResultList();
            log.debug("FindAll successfull.");
            return resourceList;
        } catch (RuntimeException re) {
            log.error("FindAll failed.", re);
            return new ArrayList<T>();
        }
    }

    @SuppressWarnings("unchecked")
	public T findByName(String name) {
        log.debug("Finding Resource instance with name: " + name);
        try {
            Query query = em.createQuery("select r from " + entityClass.getSimpleName()
                    + " r where r.name like :name");
            query.setParameter("name", name);
            T resource = (T) query.getSingleResult();
            log.debug("FindByName successfull.");
            return resource;
        } catch (RuntimeException re) {
            log.error("FindByName failed.", re);
            return null;
        }
    }
    
}
