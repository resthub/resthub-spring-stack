package org.resthub.core.context.persistence;

import java.util.Set;

import org.w3c.dom.Element;

/**
 * This class scan entities defined by a scanning configuration in application
 * context and add all found entities, matching with specified configuration
 * options, to the include list of persistence context in order to be managed
 * later (on initialization phasis)
 * 
 * @author bmeurant <Baptiste Meurant>
 */
public class PersistenceEntitiesIncluder extends
		AbstractPersistenceEntitiesParser {

	/**
	 * {@InheritDoc}
	 * 
	 * Provide specific implementation to add found entities to the include list
	 * of persistence context : ie. entities that should be managed par the
	 * current persistence unit manager of this persistence unit name
	 */
	@Override
	protected void registerResources(Set<String> entities,
			Element element) {

		PersistenceContext.getInstance().includeAll(this.getPersistenceUnitName(element), entities);
	}

}
