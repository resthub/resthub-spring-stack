package org.resthub.core.context.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.beans.factory.xml.XmlReaderContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScanBeanDefinitionParser;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/**
 * This class provide utilities for resources scanning defined by a scanning
 * configuration in application context. Define an abstract handling method to
 * manipulate all found resources, matching with specified configuration
 * options. Concrete implementations should be provided
 */
public abstract class AbstractParser extends ComponentScanBeanDefinitionParser implements ResthubBeanDefinitionParser {

    private static final String BASE_PACKAGE_ATTRIBUTE = "base-package";

    private static final String USE_DEFAULT_FILTERS_ATTRIBUTE = "use-default-filters";

    /**
     * {@InheritDoc}
     */
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        String[] basePackages = StringUtils.tokenizeToStringArray(element.getAttribute(BASE_PACKAGE_ATTRIBUTE),
                ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);

        // Actually scan for entities definitions and register them.
        AbstractClassPathScanner scanner = configureScanner(parserContext, element);
        scanner.doScan(basePackages);
        // registerResources(resources, element);

        return null;
    }

    /**
     * {@InheritDoc}
     */
    protected AbstractClassPathScanner configureScanner(ParserContext parserContext, Element element) {
        XmlReaderContext readerContext = parserContext.getReaderContext();

        boolean useDefaultFilters = isDefaultFiltersEnabled(element);

        // Delegate bean definition registration to scanner class.
        AbstractClassPathScanner scanner = createScanner(readerContext, useDefaultFilters, element);
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
            useDefaultFilters = Boolean.valueOf(element.getAttribute(USE_DEFAULT_FILTERS_ATTRIBUTE));
        }
        return useDefaultFilters;
    }

    /**
     * {@InheritDoc}
     */
    protected abstract AbstractClassPathScanner createScanner(XmlReaderContext readerContext,
            boolean useDefaultFilters, Element element);

}
