package org.resthub.core.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.core.domain.dao.StandaloneEntityDao;
import org.resthub.core.domain.model.StandaloneEntity;
import org.resthub.core.service.StandaloneEntityService;

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
	public void setResourceDao(StandaloneEntityDao resourceDao) {
		// TODO Auto-generated method stub
		super.setResourceDao(resourceDao);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<StandaloneEntity> findByName(String name) {
		return this.resourceDao.findByName(name);
	}

}
