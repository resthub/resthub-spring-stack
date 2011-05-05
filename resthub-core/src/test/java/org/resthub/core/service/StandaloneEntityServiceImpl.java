package org.resthub.core.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.core.dao.StandaloneEntityDao;
import org.resthub.core.model.StandaloneEntity;

/**
 * @author bmeurant <baptiste.meurant@gmail.com>
 * 
 *         Test dedicated interface to provide specific methods implementations
 *         for {@link StandaloneEntity} class.
 * 
 *         This allows to validate that Resthub Generic Service manages well non
 *         resource derived entities
 * 
 */
@Named("standaloneEntityService")
public class StandaloneEntityServiceImpl extends
		GenericServiceImpl<StandaloneEntity, StandaloneEntityDao, Long>
		implements StandaloneEntityService {

	@Inject
	@Named("standaloneEntityDao")
	@Override
	public void setDao(StandaloneEntityDao resourceDao) {
		// TODO Auto-generated method stub
		super.setDao(resourceDao);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<StandaloneEntity> findByName(String name) {
		return this.dao.findByName(name);
	}

}
