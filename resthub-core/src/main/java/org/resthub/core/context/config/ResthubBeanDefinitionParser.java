package org.resthub.core.context.config;

import org.springframework.beans.factory.xml.BeanDefinitionParser;

/**
 * This interface must be implemented by any BeanDefinitionParser to be
 * autodetected by ResthubNamespaceHandler.
 * 
 * Additionnaly, The implementing class must be declared in
 * META-INF/services/org.resthub.core.context.config.ResthubBeanDefinitionParser
 * file
 */
public interface ResthubBeanDefinitionParser extends BeanDefinitionParser {

    /**
     * Return the element name handled by this BeanDefinitionParser
     * 
     * @return the unqualified element name
     */
    String getElementName();

}
