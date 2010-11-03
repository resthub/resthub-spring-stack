package org.resthub.core.context.jaxb;

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
public class JAXBElementsIncluder extends
		AbstractJAXBElementsParser {

	/**
	 * {@InheritDoc}
	 * 
	 * Provide specific implementation to add found xml elements to the include list
	 * of XML binding context : ie. entities that should be managed by the
	 * current manager of this XML binding context
	 */
	@Override
	protected void registerResources(Set<String> entities,
			Element element) {

		JAXBElementsContext.getInstance().includeAll(entities);
	}

}
