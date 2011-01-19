package org.resthub.core.context.config;

import org.resthub.core.context.jaxb.ExcludeJAXBElementsParser;
import org.resthub.core.context.jaxb.IncludeJAXBElementsParser;
import org.resthub.core.context.persistence.ExcludeEntitiesParser;
import org.resthub.core.context.persistence.IncludeEntitiesParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * {@link org.springframework.beans.factory.xml.NamespaceHandler}
 * for the '<code>persistence</code>' namespace.
 *
 * @author bmeurant <Baptiste Meurant>
 */
public class ContextNamespaceHandler extends NamespaceHandlerSupport {
	
	public void init() {
		registerBeanDefinitionParser("include-entities",
				new IncludeEntitiesParser());
		registerBeanDefinitionParser("exclude-entities",
				new ExcludeEntitiesParser());
		registerBeanDefinitionParser("include-jaxb-elements",
				new IncludeJAXBElementsParser());
		registerBeanDefinitionParser("exclude-jaxb-elements",
				new ExcludeJAXBElementsParser());
	}
	
	
}
