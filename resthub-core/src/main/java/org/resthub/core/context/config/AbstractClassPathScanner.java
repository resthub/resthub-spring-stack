package org.resthub.core.context.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

/**
 * This class defines and perform scanning for resources specified
 * configurations. It reads options, parameters, filters and apply them to
 * filtering
 * 
 * This class inherit {@link ClassPathBeanDefinitionScanner} to beneficiate of
 * all options and parameters handling from Spring configuration
 */
public abstract class AbstractClassPathScanner extends ClassPathBeanDefinitionScanner {

    public AbstractClassPathScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters) {
        super(registry, useDefaultFilters);
    }

    /**
     * {@InheritDoc}
     */
    @Override
    protected abstract void registerDefaultFilters();

    /**
     * {@InheritDoc}
     * 
     */
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        List<BeanDefinition> resourcesAsBean = new ArrayList<BeanDefinition>();
        for (String basePackage : basePackages) {
            List<BeanDefinition> candidates = new ArrayList<BeanDefinition>(this.findCandidateComponents(basePackage));

            resourcesAsBean.addAll(candidates);
        }

        Set<String> resources = new HashSet<String>();
        for (BeanDefinition beanDefinition : resourcesAsBean) {
            resources.add(beanDefinition.getBeanClassName());
        }

        BeanDefinition beanDefinition = createBeanDefinition(resources);
        String beanName = BeanDefinitionReaderUtils.generateBeanName(beanDefinition, this.getRegistry());
        BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder(beanDefinition, beanName);
        registerBeanDefinition(definitionHolder, this.getRegistry());

        Set<BeanDefinitionHolder> beanList = new HashSet<BeanDefinitionHolder>();
        beanList.add(definitionHolder);

        return beanList;
    }

    protected abstract BeanDefinition createBeanDefinition(Set<String> entities);

    /**
     * {@InheritDoc}
     * 
     * Whe are no managing beans here : we deactivate initial check from
     * inherited class by always returning true
     */
    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return true;
    }

}
