package org.resthub.core.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Named;

import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author bmeurant <Baptiste Meurant>
 */
@Named("toolingService")
public class SpringToolingService implements ToolingService,
		ApplicationContextAware {

	private ApplicationContext ctx;
	private ConfigurableListableBeanFactory configurablebeanFactory;
	private List<BeanDetail> allBeans = null;

	/**
	 * {@InheritDoc}
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.ctx = applicationContext;

		// Check that the current ApplicationContext is,
		// actually a ConfigurableApplicationContext.
		// In this case, get the configurableListablebeanFactory to be able to
		// get bean definitions
		if (this.ctx instanceof ConfigurableApplicationContext) {
			this.configurablebeanFactory = ((ConfigurableApplicationContext) this.ctx)
					.getBeanFactory();
		}
	}

	/**
	 * {@InheritDoc}
	 */
	@Override
	public List<String> getBeanNames() {
		return Arrays.asList(this.ctx.getBeanDefinitionNames());
	}

	/**
	 * {@InheritDoc}
	 */
	@Override
	public List<BeanDetail> getBeanDetails() {

		if (this.allBeans == null) {

			// initialize BeanDetail list
			List<BeanDetail> beans = new ArrayList<BeanDetail>();
			BeanDetail springBeanDetail;

			// get the list of bean names
			List<String> names = this.getBeanNames();

			for (String beanName : names) {
				springBeanDetail = new BeanDetail();
				springBeanDetail.setBeanName(beanName);

				Object bean;
				String beanType;

				springBeanDetail.setAliases(Arrays.asList(this.ctx
						.getAliases(beanName)));
				bean = this.ctx.getBean(beanName);
				beanType = bean.getClass().getName();

				// Manage proxied beans
				// If the bean is proxied then its type is modiified. We
				// detect
				// it and get the correct target type
				if (AopUtils.isAopProxy(bean)) {
					beanType = AopUtils.getTargetClass(bean).getName();
					springBeanDetail.setProxied(true);
				}

				springBeanDetail.setBeanType(beanType);
				springBeanDetail.setPrototype(this.ctx
						.isPrototype(beanName));
				springBeanDetail.setSingleton(this.ctx
						.isSingleton(beanName));
				springBeanDetail.setBean(this.ctx.getBean(beanName));
				
				if (this.configurablebeanFactory != null) {

					BeanDefinition beanDefinition = this.configurablebeanFactory
							.getBeanDefinition(beanName);
					springBeanDetail.setBeanType(beanDefinition.getBeanClassName());
					springBeanDetail.setDescription(beanDefinition.getDescription());
					springBeanDetail.setScope(beanDefinition.getScope());
					springBeanDetail.setLazyInit(beanDefinition.isLazyInit());
					springBeanDetail.setAbstract(beanDefinition.isAbstract());

				} 
				beans.add(springBeanDetail);
			}
			this.allBeans = beans;
		}

		return this.allBeans;
	}

}
