package org.resthub.jpa;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages="org.resthub.jpa.repository")
public class TestConfigurer {
    
}
