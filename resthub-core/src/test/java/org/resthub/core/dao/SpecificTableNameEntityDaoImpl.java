package org.resthub.core.dao;

import javax.inject.Named;

import org.resthub.core.model.SpecificTableNameEntity;

/**
 * Class used to test dedicated interface to provide specific methods for
 * {@link SpecificTableNameEntity} class.
 * 
 * This allows to validate that Resthub Generic Dao manages well entities named
 * differently than their classes
 */
@Named("specificTableNameEntityDao")
public class SpecificTableNameEntityDaoImpl extends GenericJpaDao<SpecificTableNameEntity, Long> {

}
