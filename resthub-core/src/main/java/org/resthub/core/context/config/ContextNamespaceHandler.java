package org.resthub.core.context.config;

import org.resthub.core.context.persistence.PersistenceEntitiesExcluder;
import org.resthub.core.context.persistence.PersistenceEntitiesIncluder;
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
				new PersistenceEntitiesIncluder());
		registerBeanDefinitionParser("exclude-entities",
				new PersistenceEntitiesExcluder());
	}
	
	
}
