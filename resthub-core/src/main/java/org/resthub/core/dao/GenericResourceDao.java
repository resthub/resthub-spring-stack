package org.resthub.core.dao;

import org.resthub.core.model.Resource;

/**
 * Generic Dao that can manage {@link Resource} inherited classes.
 */
public interface GenericResourceDao<T extends Resource> extends GenericDao<T, Long> {
	
}
