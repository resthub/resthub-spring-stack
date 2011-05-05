package org.resthub.test.dbunit.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * {@link org.springframework.beans.factory.xml.NamespaceHandler}
 * for the '<code>resthub-test</code>' namespace.
 *
 * @author vanackej
 */
public class DBUnitNamespaceHandler extends NamespaceHandlerSupport {
	
	public void init() {
		registerBeanDefinitionParser("configuration",
				new DBUnitConfigurationParser());
	}
	
	
}

