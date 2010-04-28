package org.resthub.core.service;

import java.util.List;

import org.resthub.core.domain.model.Resource;

/**
 * Generic Resource Service interface.
 * @param <T> Resource class
 */
public interface GenericResourceService<T extends Resource> extends GenericService<T, Long> {

	/**
     * Find resource by reference.
     * @param ref Resource reference
     * @return resource
     */
	List<T> findByRef(String ref);
    
}
