package org.resthub.core.domain.dao.jpa;

import static org.synyx.hades.dao.query.QueryUtils.getQueryString;

import java.util.List;

import javax.persistence.Query;

import org.resthub.core.domain.dao.GenericResourceDao;
import org.resthub.core.domain.model.Resource;

public class GenericJpaResourceDao<T extends Resource> extends GenericJpaDao<T, Long> implements GenericResourceDao<T> {

	 @SuppressWarnings("unchecked")
	public List<T> findByRef(String ref) {
		String queryString = getQueryString("from %s where ref = :ref", getDomainClass());
		Query query = getEntityManager().createQuery(queryString);
		query.setParameter("ref", ref);
		return query.getResultList();
	}
    
}
