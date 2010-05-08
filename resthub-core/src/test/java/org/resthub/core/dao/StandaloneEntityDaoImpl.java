package org.resthub.core.dao;

import static org.synyx.hades.dao.query.QueryUtils.getQueryString;

import java.util.List;

import javax.inject.Named;
import javax.persistence.Query;

import org.resthub.core.dao.jpa.GenericJpaDao;
import org.resthub.core.model.StandaloneEntity;

/**
 * @author bmeurant <baptiste.meurant@gmail.com>
 * 
 * Test dedicated interface to provide specific methods for
 * {@link StandaloneEntity} class.
 * 
 * This allows to validate that Resthub Genric Dao manages well non resource
 * derived entities
 * 
 */
@Named("standaloneEntityDao")
public class StandaloneEntityDaoImpl extends GenericJpaDao<StandaloneEntity, Long> implements StandaloneEntityDao {
	
	@SuppressWarnings("unchecked")
	public List<StandaloneEntity> findByName(String name) {
		String queryString = getQueryString("from %s where name = :name", getDomainClass());
		Query query = getEntityManager().createQuery(queryString);
		query.setParameter("name", name);
		return query.getResultList();
	}

}
