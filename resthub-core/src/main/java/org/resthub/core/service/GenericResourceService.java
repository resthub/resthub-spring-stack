package org.resthub.core.service;

import java.util.List;

import org.resthub.core.model.Resource;

/**
 * Generic Resource Service
 * @param <T> Domain model class managed, inherited from {link Resource}
 */
public interface GenericResourceService<T extends Resource> extends GenericService<T, Long> {

	/**
     * Find resource by reference.
     * @param ref Resource reference
     * @return resource
     */
	List<T> findByRef(String ref);
    
}
