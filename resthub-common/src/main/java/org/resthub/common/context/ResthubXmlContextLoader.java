package org.resthub.common.context;

import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.test.context.support.GenericXmlContextLoader;

/**
 * Extends default GenericXmlContextLoader provided by Spring 3 in order to
 * disable XSD validation of application context files, in order to make
 * application startup time faster and allow using expression like ${variable}
 * as attribute values. XSD validation usually make this impossible due to
 * strict type checks.
 * 
 * This context loader can be used in you unit tests by adding the following
 * annotation :
 * 
 * @ContextConfiguration(loader=ResthubXmlContextLoader.class)
 * 
 * @see org.resthub.common.context.ResthubXmlWebApplicationContext
 */
public class ResthubXmlContextLoader extends GenericXmlContextLoader {

    @Override
    protected BeanDefinitionReader createBeanDefinitionReader(GenericApplicationContext context) {
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(context);
        reader.setValidating(false);
        return reader;
    }

}
