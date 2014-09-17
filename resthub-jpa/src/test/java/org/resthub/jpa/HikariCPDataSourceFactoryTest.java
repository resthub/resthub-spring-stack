package org.resthub.jpa;

import com.zaxxer.hikari.HikariCPTestDataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.TestElf;
import org.fest.assertions.api.Assertions;
import org.h2.jdbcx.JdbcDataSource;
import org.resthub.jpa.pool.HikariCPDataSourceFactory;
import org.resthub.jpa.sql.FakeDataSource;
import org.testng.annotations.Test;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Properties;

public class HikariCPDataSourceFactoryTest {

    private HikariCPDataSourceFactory hikariCPDataSourceFactory = new HikariCPDataSourceFactory();

    @Test
    public void testHikariDataSourceConfigNoProps() throws Exception {

        Properties configProps = new Properties();
        configProps.put("dataSourceClassName", "org.resthub.jpa.sql.FakeDataSource");

        DataSource dataSource = hikariCPDataSourceFactory.create(HikariCPTestDataSource.class, configProps);
        Assertions.assertThat(dataSource).isNotNull().isInstanceOf(HikariCPTestDataSource.class);

        HikariCPTestDataSource testDataSource = (HikariCPTestDataSource) dataSource;
        Assertions.assertThat(testDataSource).isNotNull();
        Assertions.assertThat(testDataSource.getConfig()).isNotNull();

        // check that Hikari defaults are kept
        HikariConfig expectedDefaults = new HikariConfig();
        Assertions.assertThat(testDataSource.getConfig().getConnectionCustomizerClassName()).isEqualTo(expectedDefaults.getConnectionCustomizerClassName());
        Assertions.assertThat(testDataSource.getConfig().getConnectionInitSql()).isEqualTo(expectedDefaults.getConnectionInitSql());
        Assertions.assertThat(testDataSource.getConfig().getConnectionTestQuery()).isEqualTo(expectedDefaults.getConnectionTestQuery());
        Assertions.assertThat(testDataSource.getConfig().getConnectionTimeout()).isEqualTo(expectedDefaults.getConnectionTimeout());
        Assertions.assertThat(testDataSource.getConfig().getDataSourceClassName()).isEqualTo((String)configProps.get("dataSourceClassName"));
        Assertions.assertThat(testDataSource.getConfig().getDataSource()).isEqualTo(expectedDefaults.getDataSource());
        Assertions.assertThat(testDataSource.getConfig().getIdleTimeout()).isEqualTo(expectedDefaults.getIdleTimeout());
        Assertions.assertThat(testDataSource.getConfig().getLeakDetectionThreshold()).isEqualTo(expectedDefaults.getLeakDetectionThreshold());
        Assertions.assertThat(testDataSource.getConfig().getMaximumPoolSize()).isEqualTo(expectedDefaults.getMaximumPoolSize());
        Assertions.assertThat(testDataSource.getConfig().getMaxLifetime()).isEqualTo(expectedDefaults.getMaxLifetime());
        Assertions.assertThat(testDataSource.getConfig().getPoolName()).startsWith("HikariPool-");
        Assertions.assertThat(testDataSource.getConfig().getTransactionIsolation()).isEqualTo(expectedDefaults.getTransactionIsolation());
        Assertions.assertThat(testDataSource.getConfig().isAutoCommit()).isEqualTo(expectedDefaults.isAutoCommit());
        Assertions.assertThat(testDataSource.getConfig().isJdbc4ConnectionTest()).isEqualTo(expectedDefaults.isJdbc4ConnectionTest());
        Assertions.assertThat(testDataSource.getConfig().isInitializationFailFast()).isEqualTo(expectedDefaults.isInitializationFailFast());
        Assertions.assertThat(testDataSource.getConfig().isRegisterMbeans()).isEqualTo(expectedDefaults.isRegisterMbeans());

        // check concrete datasource parameters
        Assertions.assertThat(testDataSource.isWrapperFor(FakeDataSource.class)).isTrue();
    }

    @Test
    public void testHikariDataSourceConfigAllProps() throws Exception {

        Properties configProps = new Properties();
        configProps.put("dataSourceClassName", "org.resthub.jpa.sql.FakeDataSource");
        configProps.put("dataSource.url", "jdbc:h2:mem:resthub");
        configProps.put("dataSource.user", "sa");
        configProps.put("dataSource.password", "");
        configProps.put("connectionCustomizerClassName", "com.zaxxer.hikari.TestConnectionCustomization");
        configProps.put("connectionInitSql", "select 1");
        configProps.put("connectionTestQuery", "select 1");
        configProps.put("connectionTimeout", 1000L);
        configProps.put("dataSourceClassName", "org.resthub.jpa.sql.FakeDataSource");
        configProps.put("idleTimeout", 60000L);
        configProps.put("leakDetectionThreshold", 100000L);
        configProps.put("minimumIdle", 2);
        configProps.put("maximumPoolSize", 10);
        configProps.put("maxLifetime", 200000L);
        configProps.put("poolName", "testPoolName");
        configProps.put("transactionIsolation", "TRANSACTION_READ_COMMITTED");
        configProps.put("autoCommit", false);
        configProps.put("jdbc4ConnectionTest", false);
        configProps.put("initializationFailFast", true);
        configProps.put("registerMbeans", true);

        DataSource dataSource = hikariCPDataSourceFactory.create(HikariCPTestDataSource.class, configProps);
        Assertions.assertThat(dataSource).isNotNull().isInstanceOf(HikariCPTestDataSource.class);

        HikariCPTestDataSource testDataSource = (HikariCPTestDataSource) dataSource;
        Assertions.assertThat(testDataSource).isNotNull();
        Assertions.assertThat(testDataSource.getConfig()).isNotNull();

        Assertions.assertThat(testDataSource.getConfig().getConnectionCustomizerClassName()).isEqualTo((String) configProps.get("connectionCustomizerClassName"));
        Assertions.assertThat(testDataSource.getConfig().getConnectionInitSql()).isEqualTo((String) configProps.get("connectionInitSql"));
        Assertions.assertThat(testDataSource.getConfig().getConnectionTestQuery()).isEqualTo((String) configProps.get("connectionTestQuery"));
        Assertions.assertThat(testDataSource.getConfig().getConnectionTimeout()).isEqualTo((Long) configProps.get("connectionTimeout"));
        Assertions.assertThat(testDataSource.getConfig().getDataSourceClassName()).isEqualTo((String) configProps.get("dataSourceClassName"));
        Assertions.assertThat(testDataSource.getConfig().getIdleTimeout()).isEqualTo((Long) configProps.get("idleTimeout"));
        Assertions.assertThat(testDataSource.getConfig().getLeakDetectionThreshold()).isEqualTo((Long) configProps.get("leakDetectionThreshold"));
        Assertions.assertThat(testDataSource.getConfig().getMaximumPoolSize()).isEqualTo((Integer) configProps.get("maximumPoolSize"));
        Assertions.assertThat(testDataSource.getConfig().getMaxLifetime()).isEqualTo((Long) configProps.get("maxLifetime"));
        Assertions.assertThat(testDataSource.getConfig().getPoolName()).isEqualTo((String) configProps.get("poolName"));

        Field field = Connection.class.getField((String) configProps.get("transactionIsolation"));
        int level = field.getInt(null);
        Assertions.assertThat(testDataSource.getConfig().getTransactionIsolation()).isEqualTo(level);
        Assertions.assertThat(testDataSource.getConfig().isAutoCommit()).isEqualTo((Boolean) configProps.get("autoCommit"));
        Assertions.assertThat(testDataSource.getConfig().isJdbc4ConnectionTest()).isEqualTo((Boolean) configProps.get("jdbc4ConnectionTest"));
        Assertions.assertThat(testDataSource.getConfig().isInitializationFailFast()).isEqualTo((Boolean) configProps.get("initializationFailFast"));
        Assertions.assertThat(testDataSource.getConfig().isRegisterMbeans()).isEqualTo((Boolean) configProps.get("registerMbeans"));

        // check concrete datasource parameters
        Assertions.assertThat(testDataSource.isWrapperFor(FakeDataSource.class)).isTrue();
        JdbcDataSource ds = (JdbcDataSource) TestElf.getPool(testDataSource).getDataSource();
        Assertions.assertThat(ds.getURL()).isNotNull().isEqualTo((String) configProps.get("dataSource.url"));
        Assertions.assertThat(ds.getUser()).isNotNull().isEqualTo((String) configProps.get("dataSource.user"));
        Assertions.assertThat(ds.getPassword()).isNotNull().isEqualTo((String) configProps.get("dataSource.password"));
    }


    @Test
    public void testHikariDataSourceConfigPartialAnUnresolvedProps() throws Exception {

        Properties configProps = new Properties();
        configProps.put("dataSourceClassName", "org.resthub.jpa.sql.FakeDataSource");
        configProps.put("dataSource.url", "jdbc:h2:mem:resthub");
        configProps.put("dataSource.user", "sa");
        configProps.put("dataSource.password", "");
        configProps.put("poolName", "testPoolName");
        configProps.put("maximumPoolSize", "${maximumPoolSize}");
        configProps.put("registerMbeans", "${registerMbeans}");
        configProps.put("idleTimeout", "${idleTimeout}");

        DataSource dataSource = hikariCPDataSourceFactory.create(HikariCPTestDataSource.class, configProps);
        Assertions.assertThat(dataSource).isNotNull().isInstanceOf(HikariCPTestDataSource.class);

        HikariCPTestDataSource testDataSource = (HikariCPTestDataSource) dataSource;
        Assertions.assertThat(testDataSource).isNotNull();
        Assertions.assertThat(testDataSource.getConfig()).isNotNull();

        // check that Hikari defaults are kept
        HikariConfig expectedDefaults = new HikariConfig();
        Assertions.assertThat(testDataSource.getConfig().getConnectionCustomizerClassName()).isEqualTo(expectedDefaults.getConnectionCustomizerClassName());
        Assertions.assertThat(testDataSource.getConfig().getConnectionInitSql()).isEqualTo(expectedDefaults.getConnectionInitSql());
        Assertions.assertThat(testDataSource.getConfig().getConnectionTestQuery()).isEqualTo(expectedDefaults.getConnectionTestQuery());
        Assertions.assertThat(testDataSource.getConfig().getConnectionTimeout()).isEqualTo(expectedDefaults.getConnectionTimeout());
        Assertions.assertThat(testDataSource.getConfig().getDataSource()).isEqualTo(expectedDefaults.getDataSource());
        Assertions.assertThat(testDataSource.getConfig().getIdleTimeout()).isEqualTo(expectedDefaults.getIdleTimeout());
        Assertions.assertThat(testDataSource.getConfig().getLeakDetectionThreshold()).isEqualTo(expectedDefaults.getLeakDetectionThreshold());
        Assertions.assertThat(testDataSource.getConfig().getMaximumPoolSize()).isEqualTo(expectedDefaults.getMaximumPoolSize());
        Assertions.assertThat(testDataSource.getConfig().getMaxLifetime()).isEqualTo(expectedDefaults.getMaxLifetime());
        Assertions.assertThat(testDataSource.getConfig().getTransactionIsolation()).isEqualTo(expectedDefaults.getTransactionIsolation());
        Assertions.assertThat(testDataSource.getConfig().isAutoCommit()).isEqualTo(expectedDefaults.isAutoCommit());
        Assertions.assertThat(testDataSource.getConfig().isJdbc4ConnectionTest()).isEqualTo(expectedDefaults.isJdbc4ConnectionTest());
        Assertions.assertThat(testDataSource.getConfig().isInitializationFailFast()).isEqualTo(expectedDefaults.isInitializationFailFast());
        Assertions.assertThat(testDataSource.getConfig().isRegisterMbeans()).isEqualTo(expectedDefaults.isRegisterMbeans());

        // check concrete datasource parameters
        Assertions.assertThat(testDataSource.isWrapperFor(FakeDataSource.class)).isTrue();
        JdbcDataSource ds = (JdbcDataSource) TestElf.getPool(testDataSource).getDataSource();
        Assertions.assertThat(testDataSource.getConfig().getDataSourceClassName()).isEqualTo((String) configProps.get("dataSourceClassName"));
        Assertions.assertThat(testDataSource.getConfig().getPoolName()).isEqualTo((String) configProps.get("poolName"));
        Assertions.assertThat(ds.getURL()).isNotNull().isEqualTo((String) configProps.get("dataSource.url"));
        Assertions.assertThat(ds.getUser()).isNotNull().isEqualTo((String) configProps.get("dataSource.user"));
        Assertions.assertThat(ds.getPassword()).isNotNull().isEqualTo((String) configProps.get("dataSource.password"));
    }

}
