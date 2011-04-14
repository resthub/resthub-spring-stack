package org.resthub.core.context.config.persistence;

import org.resthub.core.context.config.AbstractParser;
import org.resthub.core.context.persistence.EntityListBean;
import org.springframework.beans.factory.xml.XmlReaderContext;
import org.w3c.dom.Element;

/**
 * This class provide utilities for entities scanning defined by a scanning
 * configuration in application context. Define an abstract handling method to
 * manipulate all found entities, matching with specified configuration options.
 * Concrete implementations should be provided
 * 
 * @author bmeurant <Baptiste Meurant>
 */
public abstract class AbstractEntitesParser extends
		AbstractParser {

	private static final String DEFAULT_PERSISTENCE_UNIT_NAME = "resthub";
	private static final String PERSISTENCE_UNIT_NAME = "persistence-unit-name";

	protected abstract Class<? extends EntityListBean> getBeanClass();
	
	/**
	 * Determines in which persistence unit name entities should be added. This
	 * can be specified as a configuration option. If not, default persistence
	 * unit name is used
	 * 
	 * @param element
	 *            configuration element
	 * @return the persistence unit name
	 */
	protected String getPersistenceUnitName(Element element) {
		String persistenceUnitName = DEFAULT_PERSISTENCE_UNIT_NAME;

		if (element.hasAttribute(PERSISTENCE_UNIT_NAME)) {
			persistenceUnitName = element.getAttribute(PERSISTENCE_UNIT_NAME);
		}

		return persistenceUnitName;
	}

	/**
	 * {@InheritDoc}
	 */
	protected ClassPathEntityScanner createScanner(
			XmlReaderContext readerContext, boolean useDefaultFilters, Element element) {
		return new ClassPathEntityScanner(readerContext
				.getRegistry(), useDefaultFilters, this.getPersistenceUnitName(element), this.getBeanClass());
	}

}
