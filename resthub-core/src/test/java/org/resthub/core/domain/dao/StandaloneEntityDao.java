package org.resthub.core.domain.dao;

import java.util.List;

import org.resthub.core.domain.model.StandaloneEntity;

/**
 * @author bmeurant <baptiste.meurant@gmail.com>
 * 
 * Test dedicated interface to provide specific methods for
 * {@link StandaloneEntity} class.
 * 
 * This allows to validate that Resthub Genric Dao manages well non resource
 * derived entities
 * 
 */
public interface StandaloneEntityDao extends GenericDao<StandaloneEntity, Long> {
	List<StandaloneEntity> findByName(String name);

}
