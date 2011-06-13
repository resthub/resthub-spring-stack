package org.resthub.core.service;

import java.util.List;

import org.resthub.core.model.StandaloneEntity;

/**
 * Test dedicated interface to provide specific methods for
 * {@link StandaloneEntity} class.
 * 
 * This allows to validate that Resthub Generic Service manages well non
 * resource derived entities
 * 
 */
public interface StandaloneEntityService extends GenericService<StandaloneEntity, Long> {

    /**
     * Find a list of {@link StandaloneEntity} from name
     * 
     * @param name
     *            name to serach for
     * 
     * @return the list of found entities
     */
    List<StandaloneEntity> findByName(String name);

}
