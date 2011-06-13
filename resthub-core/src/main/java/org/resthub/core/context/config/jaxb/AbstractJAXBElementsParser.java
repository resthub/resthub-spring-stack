package org.resthub.core.context.config.jaxb;

import org.resthub.core.context.config.AbstractClassPathScanner;
import org.resthub.core.context.config.AbstractParser;
import org.resthub.core.context.jaxb.JAXBElementListBean;
import org.springframework.beans.factory.xml.XmlReaderContext;
import org.w3c.dom.Element;

/**
 * This class provide utilities for xml binding resources scanning defined by a
 * scanning configuration in application context. Define an abstract handling
 * method to manipulate all found resources, matching with specified
 * configuration options. Concrete implementations should be provided
 */
public abstract class AbstractJAXBElementsParser extends AbstractParser {

    protected abstract Class<? extends JAXBElementListBean> getBeanClass();

    /**
     * {@InheritDoc}
     */
    protected AbstractClassPathScanner createScanner(XmlReaderContext readerContext, boolean useDefaultFilters,
            Element element) {
        return new ClassPathJAXBElementsScanner(readerContext.getRegistry(), useDefaultFilters, this.getBeanClass());
    }

}
