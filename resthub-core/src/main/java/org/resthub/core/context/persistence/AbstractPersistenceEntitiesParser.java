package org.resthub.core.context.persistence;

import java.util.Set;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.beans.factory.xml.XmlReaderContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScanBeanDefinitionParser;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/**
 * This class provide utilities for entities scanning defined by a scanning
 * configuration in application context. Define an abstract handling method to
 * manipulate all found entities, matching with specified configuration options.
 * Concrete implementations should be provided
 * 
 * @author bmeurant <Baptiste Meurant>
 */
public abstract class AbstractPersistenceEntitiesParser extends
		ComponentScanBeanDefinitionParser {

	private static final String DEFAULT_PERSISTENCE_UNIT_NAME = "resthub";

	private static final String BASE_PACKAGE_ATTRIBUTE = "base-package";

	private static final String USE_DEFAULT_FILTERS_ATTRIBUTE = "use-default-filters";

	private static final String PERSISTENCE_UNIT_NAME = "persistence-unit-name";

	/**
	 * {@InheritDoc}
	 */
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		String[] basePackages = StringUtils.tokenizeToStringArray(element
				.getAttribute(BASE_PACKAGE_ATTRIBUTE),
				ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);

		// Actually scan for entities definitions and register them.
		ClassPathPersistenceDefinitionScanner scanner = configureScanner(
				parserContext, element);
		Set<String> entities = scanner.performScan(basePackages);
		registerEntitiesInPersistenceContext(entities, element,
				getPersistenceUnitName(element));

		return null;
	}

	protected abstract void registerEntitiesInPersistenceContext(Set<String> entities,
			Element element, String persistenceUnitName);

	/**
	 * Determines in which persistence unit name entities should be added. This
	 * can be specified as a configuration option. If not, default persistence
	 * unit name is used
	 * 
	 * @param element
	 *            configuration element
	 * @return the persistence unit name
	 */
	private String getPersistenceUnitName(Element element) {
		String persistenceUnitName = DEFAULT_PERSISTENCE_UNIT_NAME;

		if (element.hasAttribute(PERSISTENCE_UNIT_NAME)) {
			persistenceUnitName = element.getAttribute(PERSISTENCE_UNIT_NAME);
		}

		if (element.hasAttribute("persistence-unit")) {
			persistenceUnitName = element.getAttribute("persistence-unit");
		}
		return persistenceUnitName;
	}

	/**
	 * {@InheritDoc}
	 */
	protected ClassPathPersistenceDefinitionScanner configureScanner(
			ParserContext parserContext, Element element) {
		XmlReaderContext readerContext = parserContext.getReaderContext();

		boolean useDefaultFilters = isDefaultFiltersEnabled(element);

		// Delegate bean definition registration to scanner class.
		ClassPathPersistenceDefinitionScanner scanner = createScanner(
				readerContext, useDefaultFilters);
		scanner.setResourceLoader(readerContext.getResourceLoader());

		parseTypeFilters(element, scanner, readerContext, parserContext);

		return scanner;
	}

	/**
	 * Read configuration to decide default filters are activated or not
	 * 
	 * @param element
	 *            configuration element
	 * @return true if defaultFilters are activated, false otherwise
	 */
	private boolean isDefaultFiltersEnabled(Element element) {
		boolean useDefaultFilters = true;
		if (element.hasAttribute(USE_DEFAULT_FILTERS_ATTRIBUTE)) {
			useDefaultFilters = Boolean.valueOf(element
					.getAttribute(USE_DEFAULT_FILTERS_ATTRIBUTE));
		}
		return useDefaultFilters;
	}

	/**
	 * {@InheritDoc}
	 */
	protected ClassPathPersistenceDefinitionScanner createScanner(
			XmlReaderContext readerContext, boolean useDefaultFilters) {
		return new ClassPathPersistenceDefinitionScanner(readerContext
				.getRegistry(), useDefaultFilters);
	}

}
