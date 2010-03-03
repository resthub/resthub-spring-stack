package org.resthub.core.domain.dao;

import java.util.List;

/**
 * Generic DAO interface.
 * @param <T> Resource class
 */
public interface ResourceDao<T> {

    /**
     * Save Resource.
     * @param resource Resource to save
     * @return the persited resource
     */
    T save(T resource);

    /**
     * Delete Resource.
     * @param resource Resource to delete
     */
    void delete(T resource);

    /**
     * Delete Resource by id.
     * @param id Resource ID to delete
     */
    void delete(Long id);

    /**
     * Find Resource by id.
     *
     * @param id the resource id
     * @return the resource
     */
    T findById(Long id);

    /**
     * Count number of resource.
     * @return number of entity
     */
    Long count();

    /**
     * Get all Resources (in scrollable resulset).
     * @param offset offset
     * @param limit limit
     * @return list of Resources.
     */
    List<T> findAll(Integer offset, Integer limit);

    /**
     * flush update stack.
     */
    void flush();
}
