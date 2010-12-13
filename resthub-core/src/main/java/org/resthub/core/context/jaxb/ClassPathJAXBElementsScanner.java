package org.resthub.core.context.jaxb;

import java.lang.annotation.Annotation;

import org.resthub.core.context.AbstractClassPathScanner;
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

	public ClassPathJAXBElementsScanner(BeanDefinitionRegistry registry,
			boolean useDefaultFilters) {
		super(registry, useDefaultFilters);
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
			// javax.persistence.MappedSuperclass not available - simply skip.
		}
	}
}
