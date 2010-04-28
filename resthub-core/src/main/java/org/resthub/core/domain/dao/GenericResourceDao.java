package org.resthub.core.domain.dao;

import java.util.List;

import org.resthub.core.domain.model.Resource;

public interface GenericResourceDao<T extends Resource> extends GenericDao<T, Long> {
	
	List<T> findByRef(String ref);
	
}
