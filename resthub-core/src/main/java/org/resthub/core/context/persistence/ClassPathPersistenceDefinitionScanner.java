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

/**
 * This class defines and perform scanning for persistence unit specified
 * configurations. It reads options, parameters, filters and apply them to
 * filtering
 * 
 * This class inherit {@link ClassPathBeanDefinitionScanner} to beneficiate of
 * all options and parameters handling from Spring configuration
 * 
 * @author bmeurant <Baptiste Meurant>
 * 
 */
public class ClassPathPersistenceDefinitionScanner extends
		ClassPathBeanDefinitionScanner {

	public ClassPathPersistenceDefinitionScanner(
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

	/**
	 * Do the scan : retrive entities from classpath matching with definition
	 * and options
	 * 
	 * @param basePackages
	 *            : package name definition to search on
	 * @return the list of entities names
	 */
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

	/**
	 * {@InheritDoc}
	 * 
	 * Not implemented method, replaced by {@link #performScan(String...)}
	 * because this inerited method handle only beans and BeanDefinition that
	 * are actually useless here
	 */
	protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@InheritDoc}
	 * 
	 * Whe are no managing beans here : we deactivate initial check from
	 * inherited class by always returning true
	 */
	@Override
	protected boolean isCandidateComponent(
			AnnotatedBeanDefinition beanDefinition) {
		return true;
	}

}
