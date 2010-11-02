package org.resthub.core.context.persistence;

import java.lang.annotation.Annotation;

import org.resthub.core.context.AbstractClassPathScanner;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

/**
 * This class defines and perform scanning for persistence unit specified
 * configurations. It reads options, parameters, filters and apply them to
 * filtering
 * 
 * This class inherit {@link AbstractClassPathScanner}
 * 
 * @author bmeurant <Baptiste Meurant>
 * 
 */
public class ClassPathPersistenceScanner extends
		AbstractClassPathScanner {

	public ClassPathPersistenceScanner(
			BeanDefinitionRegistry registry, boolean useDefaultFilters) {
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
							.loadClass("javax.persistence.MappedSuperclass")),
					false));
			logger
					.info("javax.persistence.MappedSuperclass found and supported for entity scanning");
		} catch (ClassNotFoundException ex) {
			// javax.persistence.MappedSuperclass not available - simply skip.
		}
		try {
			this.addIncludeFilter(new AnnotationTypeFilter(
					((Class<? extends Annotation>) cl
							.loadClass("javax.persistence.Entity")), false));
			logger
					.info("javax.persistence.Entity annotation found and supported for entity scanning");
		} catch (ClassNotFoundException ex) {
			// javax.persistence.Entity not available - simply skip.
		}
	}
}
