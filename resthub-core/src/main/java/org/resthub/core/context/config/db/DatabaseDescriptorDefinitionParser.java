package org.resthub.core.context.config.db;

import org.resthub.core.context.config.ResthubBeanDefinitionParser;
import org.resthub.core.util.db.DatabaseDescriptorFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * This class is a simple Bean definition parser for {@link DatabaseDescriptor} initialization.
 * 
 * @author a131199
 *
 */
public class DatabaseDescriptorDefinitionParser extends AbstractSingleBeanDefinitionParser implements ResthubBeanDefinitionParser {

	public static final String DATASOURCE_ATTRIBUTE = "data-source";
	
	@Override
	protected String getBeanClassName(Element element) {
		return DatabaseDescriptorFactory.class.getCanonicalName();
	}

	@Override
	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		builder.addPropertyReference("dataSource", element.getAttribute(DATASOURCE_ATTRIBUTE));
	}

	@Override
	protected boolean shouldGenerateIdAsFallback() {
		return true;
	}

	@Override
	public String getElementName() {
		return "db-descriptor";
	}
	
}
