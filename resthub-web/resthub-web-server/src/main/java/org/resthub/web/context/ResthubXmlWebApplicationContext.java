package org.resthub.web.context;

import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.web.context.support.XmlWebApplicationContext;

/**
 * Extends default XmlWebApplicationContext provided by Spring to disable XSD validation of application context files in
 * order to make application startup time faster and allow using expression like ${variable} as attribute values (xsd
 * validation usually make this impossible due to strict type checks).
 *
 * In order to use it, add the following lines to your web.xml file :
 *
 * <pre>
 * {@code
 *  <context-param>
 *      <param-name>contextClass</param-name>
 *      <param-value>org.resthub.web.context.ResthubXmlWebApplicationContext</param-value>
 *  </context-param>
 * }</pre>
 *
 * Or use the following anotation in your test :
 * <pre>
 * {@code @ContextConfiguration(loader = ResthubXmlContextLoader.class) }
 * </pre>
 *
 */
public class ResthubXmlWebApplicationContext extends XmlWebApplicationContext {

    @Override
    protected void initBeanDefinitionReader(XmlBeanDefinitionReader beanDefinitionReader) {
        beanDefinitionReader.setValidating(false);
    }
}
