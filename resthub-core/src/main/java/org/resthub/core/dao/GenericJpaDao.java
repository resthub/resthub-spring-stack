package org.resthub.core.dao;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Query;

import org.resthub.core.util.ClassUtils;

/**
 * JPA implementation of our Generic Dao that can manage any kind of entities.
 * Extends <a href="http://hades.synyx.org/static/2.x/site/org.synyx.hades/apidocs/org/synyx/hades/dao/orm/GenericJpaDao.html">Hades GenericJpaDao</a>.
 * 
 * @see <a href="http://hades.synyx.org/static/2.x/site/org.synyx.hades/apidocs/" target="_blank">Hades 2.0 Javadoc</a>
 */
public abstract class GenericJpaDao<T, PK extends Serializable> extends
org.synyx.hades.dao.orm.GenericJpaDao<T, PK> implements GenericDao<T, PK> {
	
	@SuppressWarnings("unchecked")
	public GenericJpaDao() {
		this.setDomainClass((Class<T>)ClassUtils.getGenericType(this.getClass()));
	}
	
	@Override
	public void delete(PK id) {
		this.delete(this.readByPrimaryKey(id));
	}

	@Override
	public List<T> findLike(String propertyName, String propertyValue) {
		String queryString = "from " + this.getDomainClass().getSimpleName() + " where " + propertyName + " like :propertyValue";
		Query q = this.getEntityManager().createQuery(queryString);
		q.setParameter("propertyValue", propertyValue);
		return (List<T>) q.getResultList();
	}

	@Override
	public List<T> findEquals(String propertyName, String propertyValue) {
		String queryString = "from " + this.getDomainClass().getSimpleName() + " where " + propertyName + " = :propertyValue";
		Query q = this.getEntityManager().createQuery(queryString);
		q.setParameter("propertyValue", propertyValue);
		return (List<T>) q.getResultList();
	}
}