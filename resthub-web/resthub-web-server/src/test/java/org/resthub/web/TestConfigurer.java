package org.resthub.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages="org.resthub.web.repository")
public class TestConfigurer {
    
}
