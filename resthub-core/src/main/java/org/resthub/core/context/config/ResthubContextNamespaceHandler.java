package org.resthub.core.context.config;

import org.resthub.core.context.entities.ResthubEntitiesDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * {@link org.springframework.beans.factory.xml.NamespaceHandler}
 * for the '<code>resthub</code>' namespace.
 *
 * @author Baptiste Meurant
 */
public class ResthubContextNamespaceHandler extends NamespaceHandlerSupport {
	
	public void init() {
		registerBeanDefinitionParser("entities-scan",
				new ResthubEntitiesDefinitionParser());
	}
	
	
}
