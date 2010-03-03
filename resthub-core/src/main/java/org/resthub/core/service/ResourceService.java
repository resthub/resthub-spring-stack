package org.resthub.core.service;

import java.util.List;

/**
 * Generic Service interface.
 * @param <T> Resource class
 */
public interface ResourceService<T> {

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
    void delete(Long id);

    /**
     * Find resource by id.
     * @param id Resource id
     * @return resource
     */
    T findById(Long id);

    /**
     * Find all resources.
     * @param offset offset (default 0)
     * @param limit limit (default 100)
     * @return resources.
     */
    List<T> findAll(Integer offset, Integer limit);

    /**
     * Count all resources.
     * @return number of resources
     */
    Long count();

}
