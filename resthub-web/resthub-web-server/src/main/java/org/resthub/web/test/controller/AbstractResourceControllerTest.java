package org.resthub.web.test.controller;

import java.io.Serializable;
import org.resthub.core.model.Resource;
import org.resthub.core.service.GenericResourceService;
import org.resthub.core.util.ClassUtils;
import org.resthub.web.controller.GenericResourceController;

/**
 * Base class for your generic resource controller tests
 */
public abstract class AbstractResourceControllerTest<T extends Resource, S extends GenericResourceService<T>, C extends GenericResourceController<T, S>>
						extends AbstractControllerTest<T, Long, S, C> {

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
}
