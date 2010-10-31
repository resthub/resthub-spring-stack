package org.resthub.core.context.entities;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.beans.factory.xml.XmlReaderContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScanBeanDefinitionParser;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/**
 * Parser for the &lt;resthub:model-scan/&gt; element.
 * 
 * @author Baptiste Meurant
 */
public class ResthubEntitiesDefinitionParser extends ComponentScanBeanDefinitionParser {

	private static final String BASE_PACKAGE_ATTRIBUTE = "base-package";

	private static final String RESOURCE_PATTERN_ATTRIBUTE = "resource-pattern";

	private static final String USE_DEFAULT_FILTERS_ATTRIBUTE = "use-default-filters";


	private final Logger logger = LoggerFactory
			.getLogger(ResthubEntitiesDefinitionParser.class);

	public BeanDefinition parse(Element element, ParserContext parserContext) {
		String[] basePackages = StringUtils.tokenizeToStringArray(element
				.getAttribute(BASE_PACKAGE_ATTRIBUTE),
				ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);

		// Actually scan for bean definitions and register them.
		ClassPathEntityDefinitionScanner scanner = configureScanner(
				parserContext, element);
		List<String> entities = scanner.performScan(basePackages);
		registerComponents(entities, element);

		return null;
	}

	protected void registerComponents(List<String> entities,
			Element element) {

		String persistenceUnitName = "resthub";
		if (element.hasAttribute("persistence-unit")) {
			persistenceUnitName = element.getAttribute("persistence-unit");
		}

		ResthubEntitiesContext.getInstance().addAll(persistenceUnitName, entities);

	}

	protected ClassPathEntityDefinitionScanner configureScanner(
			ParserContext parserContext, Element element) {
		XmlReaderContext readerContext = parserContext.getReaderContext();

		boolean useDefaultFilters = true;
		if (element.hasAttribute(USE_DEFAULT_FILTERS_ATTRIBUTE)) {
			useDefaultFilters = Boolean.valueOf(element
					.getAttribute(USE_DEFAULT_FILTERS_ATTRIBUTE));
		}

		// Delegate bean definition registration to scanner class.
		ClassPathEntityDefinitionScanner scanner = createScanner(readerContext,
				useDefaultFilters);
		scanner.setResourceLoader(readerContext.getResourceLoader());

		if (element.hasAttribute(RESOURCE_PATTERN_ATTRIBUTE)) {
			scanner.setResourcePattern(element
					.getAttribute(RESOURCE_PATTERN_ATTRIBUTE));
		}

		parseTypeFilters(element, scanner, readerContext, parserContext);

		return scanner;
	}

	protected ClassPathEntityDefinitionScanner createScanner(
			XmlReaderContext readerContext, boolean useDefaultFilters) {
		return new ClassPathEntityDefinitionScanner(readerContext.getRegistry(), useDefaultFilters);
	}
	
}
