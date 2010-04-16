package org.resthub.core.domain.dao.jpa;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.resthub.core.domain.dao.ResourceGenericDao;
import org.resthub.core.domain.model.Resource;

public class JpaResourceGenericDao<T extends Resource> extends
		JpaResthubGenericDao<T, Long> implements ResourceGenericDao<T> {
	
}
