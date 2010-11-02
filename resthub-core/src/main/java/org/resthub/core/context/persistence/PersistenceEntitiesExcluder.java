package org.resthub.core.context.persistence;

import java.util.Set;

import org.w3c.dom.Element;

/**
 * This class scan entities defined by a scanning configuration in application
 * context and add all found entities, matching with specified configuration
 * options, to the exclude list of persistence context in order to be managed
 * later (on bean initialization phasis)
 * 
 * @author bmeurant <Baptiste Meurant>
 */
public class PersistenceEntitiesExcluder extends
		AbstractPersistenceEntitiesParser {

	/**
	 * {@InheritDoc}
	 * 
	 * Provide specific implementation to add found entities to the exclude list
	 * of persistence context : ie. entities that should not be managed par the
	 * current persistence unit manager of this persistence unit name, even if
	 * it has already been included
	 */
	@Override
	protected void registerResources(Set<String> entities,
			Element element) {

		PersistenceContext.getInstance().excludeAll(this.getPersistenceUnitName(element),
				entities);
	}

}
