package org.resthub.test.dbunit.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

public class DBUnitConfigurationParser extends AbstractSingleBeanDefinitionParser {

	public static final String INCLUDE_TABLE_ELEMENT = "include-table";
	public static final String EXCLUDE_TABLE_ELEMENT = "exclude-table";
	public static final String DATASOURCE_ATTRIBUTE = "data-source";
	
	@Override
	protected String getBeanClassName(Element element) {
		return DbUnitConfiguration.class.getCanonicalName();
	}

	@Override
	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		
		builder.addPropertyReference("dataSource", element.getAttribute(DATASOURCE_ATTRIBUTE));
		
		List<Element> includeElements = DomUtils.getChildElementsByTagName(element, INCLUDE_TABLE_ELEMENT);
		if (includeElements.size() > 0) {
			List<String> includes = new ArrayList<String>();
			for (Element includeElement : includeElements) {
				includes.add(includeElement.getNodeValue());
			}
			builder.addPropertyValue("includeTables", includes);	
		}
		
		List<Element> excludeElements = DomUtils.getChildElementsByTagName(element, EXCLUDE_TABLE_ELEMENT);
		if (excludeElements.size() > 0) {
			List<String> excludes = new ArrayList<String>();
			for (Element excludeElement : excludeElements) {
				excludes.add(excludeElement.getNodeValue());
			}
			builder.addPropertyValue("excludeTables", excludes);	
		}
		
	}

	@Override
	protected boolean shouldGenerateIdAsFallback() {
		return true;
	}
	
	
}
