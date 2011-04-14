package org.resthub.core.context.config;

import java.util.ServiceLoader;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * {@link org.springframework.beans.factory.xml.NamespaceHandler} for the '
 * <code>persistence</code>' namespace.
 * 
 * @author bmeurant <Baptiste Meurant>
 * @author Johann Vanackere
 */
public class ResthubNamespaceHandler extends NamespaceHandlerSupport {

	private static ServiceLoader<ResthubBeanDefinitionParser> serviceLoader = ServiceLoader.load(ResthubBeanDefinitionParser.class);
	
	public void init() {
		for (ResthubBeanDefinitionParser resthubBeanDefinitionParser : serviceLoader) {
			registerBeanDefinitionParser(resthubBeanDefinitionParser.getElementName(), resthubBeanDefinitionParser);
		}
	}

}
