package org.resthub.core.service;

import java.io.Serializable;
import java.util.List;

import org.synyx.hades.domain.Page;
import org.synyx.hades.domain.Pageable;

/**
 * Generic Service interface.
 *  @param <T> Domain model class managed, must be an Entity
 *  @param <PK> Primary key class of T
 */
public interface GenericService<T, ID extends Serializable> {

    /**
     * Create new resource.
     * @param resource Resource to create
     * @return new resource
     */
    T create(T resource);

    /**
     * Update existing resource.
     * @param resource Resource to update
     * @return resource updated
     */
    T update(T resource);

    /**
     * Delete existing resource.
     * @param resource Resource to delete
     */
    void delete(T resource);

    /**
     * Delete existing resource.
     * @param id Resource id
     */
    void delete(ID id);

    /**
     * Find resource by id.
     * @param id Resource id
     * @return resource
     */
    T findById(ID id);
    
    /**
     * Find all resources.
     * @param offset offset (default 0)
     * @param limit limit (default 100)
     * @return resources.
     */
    List<T> findAll(Integer offset, Integer limit);

    /**
     * Find all resources.
     * @return a list of all resources.
     */
    List<T> findAll();
    
    /**
     * Find all resources (pageable).
     * @param pageRequest page request
     * @return resources
     */
    Page<T> findAll(Pageable pageRequest);

    /**
     * Count all resources.
     * @return number of resources
     */
    Long count();

}
