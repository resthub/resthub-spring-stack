package org.resthub.core.domain.dao.jpa;

import org.resthub.core.domain.dao.AbstractResourceGenericDao;
import org.resthub.core.domain.model.Resource;

public class JpaResourceGenericDao<T extends Resource> extends
		JpaResthubGenericDao<T, Long> implements AbstractResourceGenericDao<T> {
	
}
