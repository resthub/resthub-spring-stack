package org.resthub.core.context.persistence.config;

import org.resthub.core.context.persistence.PersistenceEntitiesIncluder;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * {@link org.springframework.beans.factory.xml.NamespaceHandler}
 * for the '<code>persistence</code>' namespace.
 *
 * @author Baptiste Meurant
 */
public class PersistenceContextNamespaceHandler extends NamespaceHandlerSupport {
	
	public void init() {
		registerBeanDefinitionParser("include-entities",
				new PersistenceEntitiesIncluder());
	}
	
	
}
