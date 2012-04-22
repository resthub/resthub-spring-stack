package org.resthub.jpa.config.database;

import java.util.Map;
import javax.inject.Inject;
import org.apache.commons.dbcp.BasicDataSource;
import org.fest.assertions.api.Assertions;
import org.resthub.test.common.AbstractTest;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.testng.annotations.Test;

public class DefaultDatabaseConfigurationTest extends AbstractTest {

    @Inject
    BasicDataSource dataSource;

    @Inject
    LocalContainerEntityManagerFactoryBean entityManagerFactory;

    @Test
    public void testDataSource() {
        Assertions.assertThat(dataSource.getDriverClassName()).isEqualTo("org.h2.Driver");
        Assertions.assertThat(dataSource.getUrl()).isEqualTo("jdbc:h2:mem:resthub;DB_CLOSE_DELAY=-1");
        Assertions.assertThat(dataSource.getMaxActive()).isEqualTo(50);
        Assertions.assertThat(dataSource.getMaxWait()).isEqualTo(1000);
        Assertions.assertThat(dataSource.isPoolPreparedStatements()).isTrue();
        Assertions.assertThat(dataSource.getUsername()).isEqualTo("sa");
        Assertions.assertThat(dataSource.getPassword().isEmpty()).isTrue();
    }

    @Test
    public void testEntityManagerFactory() {
        Map<String, Object> jpaProperties = entityManagerFactory.getJpaPropertyMap();
        Assertions.assertThat(jpaProperties.get("hibernate.dialect")).isEqualTo("org.hibernate.dialect.H2Dialect");
        Assertions.assertThat(jpaProperties.get("hibernate.format_sql")).isEqualTo("true");
        Assertions.assertThat(jpaProperties.get("hibernate.hbm2ddl.auto")).isEqualTo("update");
        Assertions.assertThat(jpaProperties.get("hibernate.cache.use_second_level_cache")).isEqualTo("true");
        Assertions.assertThat(jpaProperties.get("hibernate.cache.provider_class")).isEqualTo("net.sf.ehcache.hibernate.SingletonEhCacheProvider");
    }
}
