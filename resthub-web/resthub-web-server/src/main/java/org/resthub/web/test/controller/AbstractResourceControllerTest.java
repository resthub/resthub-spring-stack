package org.resthub.web.test.controller;

import java.io.Serializable;
import org.resthub.core.model.Resource;
import org.resthub.core.service.GenericResourceService;
import org.resthub.core.util.ClassUtils;
import org.resthub.web.controller.GenericResourceController;

/**
 *
 * @author Guillaume Zurbach
 */
public abstract class AbstractResourceControllerTest <T extends Resource, C extends GenericResourceController<T, GenericResourceService<T>>>
						extends AbstractControllerTest {

	/**
	 * Returns the resource class
	 */
	@Override
	public Class<? extends Resource> getResourceClass() throws Exception {
		return createTestResource().getClass();
	}

	/**
	 * Creates a generic instance for tests
	 * @return T
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected T createTestResource() throws Exception {
		return (T) ClassUtils.getGenericTypeFromBean(this.controller).newInstance();
	}

	/**
	 * Returns object id
	 * @param obj
	 * @return id
	 */
	@Override
	protected Serializable getIdFromObject(Object obj) {
		return ((T)obj).getId();
	}
}
