package org.resthub.tapestry5.validation.services;

import org.apache.tapestry5.beanvalidator.BeanValidatorConfigurer;
import org.apache.tapestry5.ioc.OrderedConfiguration;

public class ResthubValidationModule {

    public static void contributeBeanValidatorSource(
            OrderedConfiguration<BeanValidatorConfigurer> configuration)
    {
        configuration.add("resthubConfigurer", new BeanValidatorConfigurer()
        {
            public void configure(javax.validation.Configuration<?> configuration)
            {
                configuration.ignoreXmlConfiguration();
            }
        });
    }
	
}
