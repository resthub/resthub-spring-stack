package org.resthub.core.context.jaxb;

import java.lang.annotation.Annotation;
import java.util.Set;

import org.resthub.core.context.AbstractClassPathScanner;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

/**
 * This class defines and perform scanning for XML binding specified
 * configurations. It reads options, parameters, filters and apply them to
 * filtering
 * 
 * This class inherit {@link AbstractClassPathScanner}
 * 
 * @author bmeurant <Baptiste Meurant>
 * 
 */
public class ClassPathJAXBElementsScanner extends AbstractClassPathScanner {

	private final Class<? extends JAXBElementListBean> beanClass;
	
	public ClassPathJAXBElementsScanner(BeanDefinitionRegistry registry,
			boolean useDefaultFilters, Class<? extends JAXBElementListBean> beanClass) {
		super(registry, useDefaultFilters);
		this.beanClass = beanClass;
	}

	protected BeanDefinition createBeanDefinition(Set<String> entities) {
		BeanDefinitionBuilder builder = BeanDefinitionBuilder
				.genericBeanDefinition();
		builder.getRawBeanDefinition().setBeanClass(beanClass);
		builder.addPropertyValue("elements", entities);
		return builder.getRawBeanDefinition();
	}
	
	/**
	 * {@InheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void registerDefaultFilters() {
		ClassLoader cl = ClassPathScanningCandidateComponentProvider.class
				.getClassLoader();
		try {
			this.addIncludeFilter(new AnnotationTypeFilter(
					((Class<? extends Annotation>) cl
							.loadClass("javax.xml.bind.annotation.XmlRootElement")),
					false));
			logger
					.info("javax.xml.bind.annotation.XmlRootElement found and supported for xml binding resource scanning");
		} catch (ClassNotFoundException ex) {
			// javax.xml.bind.annotation.XmlRootElement not available - simply skip.
		}
	}
}
