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
 * This class scan entities defined by a scanning configuration in application
 * context and add all found entities, matching with specified configuration
 * options, to the persistence context in order to be managed later (on
 * initialization phasis)
 * 
 * @author bmeurant <Baptiste Meurant>
 */
public class PersistenceEntitiesIncluder extends
		ComponentScanBeanDefinitionParser {

	private static final String BASE_PACKAGE_ATTRIBUTE = "base-package";

	private static final String USE_DEFAULT_FILTERS_ATTRIBUTE = "use-default-filters";

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
		registerEntitiesInPersistenceContext(entities, element, getPersistenceUnitName(element));

		return null;
	}

	protected void registerEntitiesInPersistenceContext(Set<String> entities, Element element,
			String persistenceUnitName) {

		PersistenceContext.getInstance().addAll(persistenceUnitName, entities);
	}

	private String getPersistenceUnitName(Element element) {
		String persistenceUnitName = "resthub";
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

		boolean useDefaultFilters = true;
		if (element.hasAttribute(USE_DEFAULT_FILTERS_ATTRIBUTE)) {
			useDefaultFilters = Boolean.valueOf(element
					.getAttribute(USE_DEFAULT_FILTERS_ATTRIBUTE));
		}

		// Delegate bean definition registration to scanner class.
		ClassPathPersistenceDefinitionScanner scanner = createScanner(
				readerContext, useDefaultFilters);
		scanner.setResourceLoader(readerContext.getResourceLoader());

		parseTypeFilters(element, scanner, readerContext, parserContext);

		return scanner;
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
