package org.resthub.core.dao;

import java.util.List;

import org.resthub.core.model.Resource;

/**
 * Generic Dao that can manage {@link Resource} inherited classes.
 */
public interface GenericResourceDao<T extends Resource> extends GenericDao<T, Long> {
	
	
}
