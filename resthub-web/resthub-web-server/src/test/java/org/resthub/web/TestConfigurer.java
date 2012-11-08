package org.resthub.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages="org.resthub.web.repository")
@ImportResource("classpath*:resthubContext.xml")
public class TestConfigurer {
    
}
