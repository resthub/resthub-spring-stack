package org.resthub.core.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.core.domain.dao.DerivedEntityDao;
import org.resthub.core.domain.model.DerivedEntity;
import org.resthub.core.service.DerivedEntityService;

/**
 * @author bmeurant <baptiste.meurant@gmail.com>
 * 
 *         Test dedicated interface to provide specific methods implementations
 *         for {@link DerivedEntity} class.
 * 
 *         This allows to validate that Resthub Generic Service manages well 
 *         resource derived entities
 * 
 */
@Named("derivedEntityService")
public class DerivedEntityServiceImpl extends
		ResourceGenericServiceImpl<DerivedEntity, DerivedEntityDao>
		implements DerivedEntityService {

	@Inject
	@Named("derivedEntityDao")
	@Override
	public void setResourceDao(DerivedEntityDao resourceDao) {
		// TODO Auto-generated method stub
		super.setResourceDao(resourceDao);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DerivedEntity> findByName(String name) {
		return this.resourceDao.findByName(name);
	}

}
