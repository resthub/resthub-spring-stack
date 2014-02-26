package org.resthub.jpa;

import org.fest.assertions.data.MapEntry;
import org.resthub.test.AbstractTransactionalTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import static org.fest.assertions.api.Assertions.assertThat;

@ActiveProfiles({"resthub-jpa", "resthub-pool-bonecp"})
@ContextConfiguration(locations = {"classpath*:resthubContext.xml", "classpath:extendedJPAApplicationContext.xml", "classpath:jpa-test-context.xml"})
public class ExtendedJPAPropertiesTest extends AbstractTransactionalTest {

    @PersistenceUnit
    private EntityManagerFactory emf;

    @Test
    public void testExtendingJPAProperties() {
        assertThat(emf).isNotNull();
        assertThat(emf.getProperties()).isNotNull().isNotEmpty();

        // not modified property
        assertThat(emf.getProperties()).contains(MapEntry.entry("hibernate.hbm2ddl.auto", "update"));

        // modified property
        assertThat(emf.getProperties()).contains(MapEntry.entry("hibernate.format_sql", "false"));

        // added property with default value
        assertThat(emf.getProperties()).contains(MapEntry.entry("my-new-prop", "my-value"));

        // added property with value in database.properties
        assertThat(emf.getProperties()).contains(MapEntry.entry("my-new-prop-in-properties", "my-other-value"));
    }

}
