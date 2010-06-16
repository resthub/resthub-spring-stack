package org.resthub.core.service;

import org.resthub.core.dao.GenericResourceDao;
import org.resthub.core.model.Resource;
/**
 * Generic Service implementation.
 * 
* @param <T> Domain model class managed, inherited from {link Resource}
 * @param <D> Resource DAO inherited class
 */
public abstract class GenericResourceServiceImpl<T extends Resource, D extends GenericResourceDao<T>> extends
		GenericServiceImpl<T, D, Long> implements
		GenericResourceService<T> {

	
}
