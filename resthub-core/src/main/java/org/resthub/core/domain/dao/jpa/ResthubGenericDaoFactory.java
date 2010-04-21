package org.resthub.core.domain.dao.jpa;

import org.synyx.hades.dao.GenericDao;
import org.synyx.hades.dao.orm.GenericDaoFactoryBean;
import org.synyx.hades.dao.orm.GenericJpaDao;

/**
 * Improve default Hades generic DAO with our own
 */
public class ResthubGenericDaoFactory<T extends GenericDao<?, ?>> extends
		GenericDaoFactoryBean<T> {

	@SuppressWarnings("unchecked")
	protected Class<? extends GenericJpaDao> getDaoClass() {
		return JpaResthubGenericDao.class;
	}
	
}
