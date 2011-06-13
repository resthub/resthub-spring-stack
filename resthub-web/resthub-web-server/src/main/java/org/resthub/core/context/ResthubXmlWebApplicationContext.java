package org.resthub.core.context;

import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.web.context.support.XmlWebApplicationContext;

/**
 * Extends default XmlWebApplicationContext provided by Spring 3 in order to
 * disable XSD validation of application context files in order to make
 * application startup time faster and allow using expression like ${variable}
 * as attribute values (xsd validation usually make this impossible due to
 * strict type checks).
 * 
 * In order to use it, add the following lines to your web.xml file :
 * <context-param>
 *      <param-name>contextClass</param-name>
 *      <param-value>org.kazansource.core.context.KazanXmlWebApplicationContext</param-value>
 * </context-param>
 * 
 * @see org.resthub.core.context.ResthubXmlContextLoader 
 */
public class ResthubXmlWebApplicationContext extends XmlWebApplicationContext {

    @Override
    protected void initBeanDefinitionReader(XmlBeanDefinitionReader beanDefinitionReader) {
        beanDefinitionReader.setValidating(false);
    }

}
