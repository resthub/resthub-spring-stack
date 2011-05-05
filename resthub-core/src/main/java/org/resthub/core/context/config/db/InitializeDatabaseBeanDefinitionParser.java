package org.resthub.core.context.config.db;

import java.util.ArrayList;
import java.util.List;

import org.resthub.core.context.config.ResthubBeanDefinitionParser;
import org.resthub.core.util.db.SelectiveDatabasePopulator;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.jdbc.config.SortedResourcesFactoryBean;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

public class InitializeDatabaseBeanDefinitionParser extends
		AbstractBeanDefinitionParser implements ResthubBeanDefinitionParser {

	@Override
	protected AbstractBeanDefinition parseInternal(Element element,
			ParserContext context) {
		BeanDefinitionBuilder builder = BeanDefinitionBuilder
				.genericBeanDefinition(DataSourceInitializer.class);
		builder.addPropertyReference("dataSource",
				element.getAttribute("data-source"));
		builder.addPropertyValue("enabled", element.getAttribute("enabled"));
		builder.addPropertyValue("databasePopulator",createSelectiveDatabasePopulator(element));
		return getSourcedBeanDefinition(builder, element, context);
	}

	@Override
	protected boolean shouldGenerateId() {
		return true;
	}

	private BeanDefinition createSelectiveDatabasePopulator(Element element) {
		
		List<Element> scripts = DomUtils.getChildElementsByTagName(element,
				"script");
		
		List<Element> exceptionElements = DomUtils.getChildElementsByTagName(element,
		"exception");
		
		boolean ignoreFailedDrops = "DROPS".equals(element.getAttribute("ignore-failures"));
		boolean continueOnError = "ALL".equals(element.getAttribute("ignore-failures"));
		
		BeanDefinitionBuilder selectivebuilder = BeanDefinitionBuilder.genericBeanDefinition(SelectiveDatabasePopulator.class);
		
		BeanDefinition defaultDbPopulator = createResourceDatabasePopulator(scripts, ignoreFailedDrops, continueOnError);
		
		selectivebuilder.addPropertyValue("databasePopulator", defaultDbPopulator);
		
		for (Element exception : exceptionElements){
			String product = exception.getAttribute("product");
			List<Element> exceptionScripts = DomUtils.getChildElementsByTagName(exception,
			"script");
			selectivebuilder.addPropertyValue("exceptions[" + product + "]", createResourceDatabasePopulator(exceptionScripts, ignoreFailedDrops, continueOnError));
		}

		return selectivebuilder.getBeanDefinition();
	}

	private BeanDefinition createResourceDatabasePopulator(List<Element> scripts, boolean ignoreFailedDrops, boolean continueOnError) {
		BeanDefinitionBuilder builder = BeanDefinitionBuilder
				.genericBeanDefinition(ResourceDatabasePopulator.class);
		builder.addPropertyValue("ignoreFailedDrops", ignoreFailedDrops);
		builder.addPropertyValue("continueOnError", continueOnError);
 
		List<String> locations = new ArrayList<String>();
		for (Element scriptElement : scripts) {
			String location = scriptElement.getAttribute("location");
			locations.add(location);
		}

		// Use a factory bean for the resources so they can be given an order if
		// a pattern is used
		BeanDefinitionBuilder resourcesFactory = BeanDefinitionBuilder
				.genericBeanDefinition(SortedResourcesFactoryBean.class);
		resourcesFactory.addConstructorArgValue(locations);
		builder.addPropertyValue("scripts",
				resourcesFactory.getBeanDefinition());

		return builder.getBeanDefinition();
	}

	private AbstractBeanDefinition getSourcedBeanDefinition(
			BeanDefinitionBuilder builder, Element source, ParserContext context) {
		AbstractBeanDefinition definition = builder.getBeanDefinition();
		definition.setSource(context.extractSource(source));
		return definition;
	}

	@Override
	public String getElementName() {
		return "db-initialize";
	}
}