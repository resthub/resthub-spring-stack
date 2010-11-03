package org.resthub.core.context.jaxb;

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
public class JAXBElementsExcluder extends
		AbstractJAXBElementsParser {

	/**
	 * {@InheritDoc}
	 * 
	 * Provide specific implementation to add found xml elements to the exclude list
	 * of XML binding context : ie. xml elements that should not be managed by the
	 * current XML binding context manager even if
	 * it have already been included
	 */
	@Override
	protected void registerResources(Set<String> elements,
			Element element) {

		JAXBElementsContext.getInstance().excludeAll(elements);
	}

}
