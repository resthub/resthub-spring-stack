package org.resthub.core.context;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

/**
 * This class defines and perform scanning for resources specified
 * configurations. It reads options, parameters, filters and apply them to
 * filtering
 * 
 * This class inherit {@link ClassPathBeanDefinitionScanner} to beneficiate of
 * all options and parameters handling from Spring configuration
 * 
 * @author bmeurant <Baptiste Meurant>
 * 
 */
public abstract class AbstractClassPathScanner extends
		ClassPathBeanDefinitionScanner {

	public AbstractClassPathScanner(
			BeanDefinitionRegistry registry, boolean useDefaultFilters) {
		super(registry, useDefaultFilters);
	}

	/**
	 * {@InheritDoc}
	 */
	@Override
	protected abstract void registerDefaultFilters();

	/**
	 * Do the scan : retrieve resources from classpath matching with definition
	 * and options
	 * 
	 * @param basePackages
	 *            : package name definition to search on
	 * @return the list of resources names
	 */
	protected Set<String> performScan(String... basePackages) {
		List<BeanDefinition> resourcesAsBean = new ArrayList<BeanDefinition>();
		for (String basePackage : basePackages) {
			List<BeanDefinition> candidates = new ArrayList<BeanDefinition>(
					this.findCandidateComponents(basePackage));

			resourcesAsBean.addAll(candidates);
		}

		Set<String> resources = new HashSet<String>();
		for (BeanDefinition beanDefinition : resourcesAsBean) {
			resources.add(beanDefinition.getBeanClassName());
		}

		return resources;
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
