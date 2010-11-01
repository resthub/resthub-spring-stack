package org.resthub.core.context.persistence;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

public class ClassPathPersistenceDefinitionScanner extends
		ClassPathBeanDefinitionScanner {

	public ClassPathPersistenceDefinitionScanner(BeanDefinitionRegistry registry,
			boolean useDefaultFilters) {
		super(registry, useDefaultFilters);
	}

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

	protected Set<String> performScan(String... basePackages) {
		List<BeanDefinition> entitiesAsBean = new ArrayList<BeanDefinition>();
		for (String basePackage : basePackages) {
			List<BeanDefinition> candidates = new ArrayList<BeanDefinition>(
					this.findCandidateComponents(basePackage));

			entitiesAsBean.addAll(candidates);
		}
		
		Set<String> entities = new HashSet<String>();
		for (BeanDefinition beanDefinition : entitiesAsBean) {
			entities.add(beanDefinition.getBeanClassName());
		}

		return entities;
	}

	protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected boolean isCandidateComponent(
			AnnotatedBeanDefinition beanDefinition) {
		return true;
	}

}
