package org.resthub.core.domain.dao.jpa;

import org.synyx.hades.dao.GenericDao;
import org.synyx.hades.dao.orm.GenericDaoFactoryBean;
import org.synyx.hades.dao.orm.GenericJpaDao;

public class ResthubGenericDaoFactory<T extends GenericDao<?, ?>> extends
		GenericDaoFactoryBean<T> {

	protected Class<? extends GenericJpaDao> getDaoClass() {
		return JpaResthubGenericDao.class;
	}
	
}
