package org.resthub.core.context.jaxb;

import java.util.Set;

import org.resthub.core.context.AbstractClassPathScanner;
import org.resthub.core.context.AbstractParser;
import org.springframework.beans.factory.xml.XmlReaderContext;
import org.w3c.dom.Element;

/**
 * This class provide utilities for xml binding resources scanning defined by a scanning
 * configuration in application context. Define an abstract handling method to
 * manipulate all found resources, matching with specified configuration options.
 * Concrete implementations should be provided
 * 
 * @author bmeurant <Baptiste Meurant>
 */
public abstract class AbstractJAXBElementsParser extends
		AbstractParser {

	/**
	 * {@InheritDoc}
	 */
	@Override
	protected abstract void registerResources(Set<String> resources,
			Element element);

	/**
	 * {@InheritDoc}
	 */
	protected AbstractClassPathScanner createScanner(
			XmlReaderContext readerContext, boolean useDefaultFilters) {
		return new ClassPathJAXBElementsScanner(readerContext
				.getRegistry(), useDefaultFilters);
	}

}
