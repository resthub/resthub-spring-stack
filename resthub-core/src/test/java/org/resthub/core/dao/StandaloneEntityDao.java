package org.resthub.core.dao;

import java.util.List;

import org.resthub.core.model.StandaloneEntity;

/**
 * Test dedicated interface to provide specific methods for
 * {@link StandaloneEntity} class.
 * 
 * This allows to validate that Resthub Genric Dao manages well non resource
 * derived entities
 */
public interface StandaloneEntityDao extends GenericDao<StandaloneEntity, Long> {
    List<StandaloneEntity> findByName(String name);

}
