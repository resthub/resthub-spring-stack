package org.resthub.core.context.config.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.resthub.core.context.config.ResthubBeanDefinitionParser;
import org.resthub.core.util.db.DynamicDataSourceInitializer;
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
				.genericBeanDefinition(DynamicDataSourceInitializer.class);
		builder.addPropertyReference("dataSource",
				element.getAttribute("data-source"));
		builder.addPropertyValue("enabled", element.getAttribute("enabled"));
		setDatabasePopulator(element, builder);
		return getSourcedBeanDefinition(builder, element, context);
	}

	@Override
	protected boolean shouldGenerateId() {
		return true;
	}

	private void setDatabasePopulator(Element element, BeanDefinitionBuilder builder) {
		List<Element> scripts = DomUtils.getChildElementsByTagName(element,
				"script");
		
		List<Element> exceptionElements = DomUtils.getChildElementsByTagName(element,
		"exception");
		
		boolean ignoreFailedDrops = element.getAttribute("ignore-failures").equals("DROPS");
		boolean continueOnError = element.getAttribute("ignore-failures").equals("ALL");
		
		if (scripts.size() > 0) {
			builder.addPropertyValue("databasePopulator",
					createDatabasePopulator(scripts, ignoreFailedDrops, continueOnError));
		}
		
		//Map<String, BeanDefinition> exceptions = new HashMap<String, BeanDefinition>();
		
		for (Element exception : exceptionElements){
			String product = exception.getAttribute("product");
			List<Element> exceptionScripts = DomUtils.getChildElementsByTagName(exception,
			"script");
			builder.addPropertyValue("exceptions[" + product + "]", createDatabasePopulator(exceptionScripts, ignoreFailedDrops, continueOnError));
			
			//exceptions.put(product, createDatabasePopulator(exceptionScripts, ignoreFailedDrops, continueOnError));
			
		}
		//builder.addPropertyValue("exceptions", exceptions);
	}

	private BeanDefinition createDatabasePopulator(List<Element> scripts, boolean ignoreFailedDrops, boolean continueOnError) {
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