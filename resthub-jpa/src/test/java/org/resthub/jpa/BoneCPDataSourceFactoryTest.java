package org.resthub.jpa;

import com.jolbox.bonecp.BoneCPConfig;
import com.jolbox.bonecp.BoneCPTestDataSource;
import org.fest.assertions.api.Assertions;
import org.resthub.jpa.pool.BoneCPDataSourceFactory;
import org.testng.annotations.Test;

import javax.sql.DataSource;
import java.util.Properties;

public class BoneCPDataSourceFactoryTest {

    private BoneCPDataSourceFactory boneCPDataSourceFactory = new BoneCPDataSourceFactory();

    @Test
    public void testBoneCPDataSourceConfigNoProps() throws Exception {

        DataSource dataSource = boneCPDataSourceFactory.create(BoneCPTestDataSource.class, new Properties());
        Assertions.assertThat(dataSource).isNotNull().isInstanceOf(BoneCPTestDataSource.class);

        BoneCPTestDataSource testDataSource = (BoneCPTestDataSource) dataSource;
        Assertions.assertThat(testDataSource).isNotNull();
        Assertions.assertThat(testDataSource.getDriverClass()).isNull();
        Assertions.assertThat(testDataSource.getConfig()).isNotNull();

        // check that BoneCP defaults are kept
        BoneCPConfig expectedDefaults = new BoneCPConfig();
        Assertions.assertThat(testDataSource.getConfig().getAcquireIncrement()).isEqualTo(expectedDefaults.getAcquireIncrement());
        Assertions.assertThat(testDataSource.getConfig().getAcquireRetryAttempts()).isEqualTo(expectedDefaults.getAcquireRetryAttempts());
        Assertions.assertThat(testDataSource.getConfig().getAcquireRetryDelayInMs()).isEqualTo(expectedDefaults.getAcquireRetryDelayInMs());
        Assertions.assertThat(testDataSource.getConfig().getCloseConnectionWatchTimeoutInMs()).isEqualTo(expectedDefaults.getCloseConnectionWatchTimeoutInMs());
        Assertions.assertThat(testDataSource.getConfig().getConnectionTimeoutInMs()).isEqualTo(expectedDefaults.getConnectionTimeoutInMs());
        Assertions.assertThat(testDataSource.getConfig().getConnectionHookClassName()).isEqualTo(expectedDefaults.getConnectionHookClassName());
        Assertions.assertThat(testDataSource.getConfig().getConnectionTestStatement()).isEqualTo(expectedDefaults.getConnectionTestStatement());
        Assertions.assertThat(testDataSource.getConfig().getDefaultAutoCommit()).isEqualTo(expectedDefaults.getDefaultAutoCommit());
        Assertions.assertThat(testDataSource.getConfig().getDefaultCatalog()).isEqualTo(expectedDefaults.getDefaultCatalog());
        Assertions.assertThat(testDataSource.getConfig().getDefaultReadOnly()).isEqualTo(expectedDefaults.getDefaultReadOnly());
        Assertions.assertThat(testDataSource.getConfig().getDefaultTransactionIsolation()).isEqualTo(expectedDefaults.getDefaultTransactionIsolation());
        Assertions.assertThat(testDataSource.getConfig().getIdleConnectionTestPeriodInMinutes()).isEqualTo(expectedDefaults.getIdleConnectionTestPeriodInMinutes());
        Assertions.assertThat(testDataSource.getConfig().getIdleMaxAgeInMinutes()).isEqualTo(expectedDefaults.getIdleMaxAgeInMinutes());
        Assertions.assertThat(testDataSource.getConfig().getInitSQL()).isEqualTo(expectedDefaults.getInitSQL());
        Assertions.assertThat(testDataSource.getConfig().getPoolName()).isEqualTo(expectedDefaults.getPoolName());
        Assertions.assertThat(testDataSource.getConfig().getMaxConnectionAgeInSeconds()).isEqualTo(expectedDefaults.getMaxConnectionAgeInSeconds());
        Assertions.assertThat(testDataSource.getConfig().getMaxConnectionsPerPartition()).isEqualTo(expectedDefaults.getMaxConnectionsPerPartition());
        Assertions.assertThat(testDataSource.getConfig().getMinConnectionsPerPartition()).isEqualTo(expectedDefaults.getMinConnectionsPerPartition());
        Assertions.assertThat(testDataSource.getConfig().getPartitionCount()).isEqualTo(expectedDefaults.getPartitionCount());
        Assertions.assertThat(testDataSource.getConfig().getPoolAvailabilityThreshold()).isEqualTo(expectedDefaults.getPoolAvailabilityThreshold());
        Assertions.assertThat(testDataSource.getConfig().getPoolStrategy()).isEqualTo(expectedDefaults.getPoolStrategy());
        Assertions.assertThat(testDataSource.getConfig().getQueryExecuteTimeLimitInMs()).isEqualTo(expectedDefaults.getQueryExecuteTimeLimitInMs());
        Assertions.assertThat(testDataSource.getConfig().getServiceOrder()).isEqualTo(expectedDefaults.getServiceOrder());
        Assertions.assertThat(testDataSource.getConfig().getStatementsCacheSize()).isEqualTo(expectedDefaults.getStatementsCacheSize());
        Assertions.assertThat(testDataSource.getConfig().getJdbcUrl()).isEqualTo(expectedDefaults.getJdbcUrl());
        Assertions.assertThat(testDataSource.getConfig().getUsername()).isEqualTo(expectedDefaults.getUsername());
        Assertions.assertThat(testDataSource.getConfig().getPassword()).isEqualTo(expectedDefaults.getPassword());
    }

    @Test
    public void testBoneCPDataSourceConfigAllProps() throws Exception {

        Properties configProps = new Properties();
        configProps.put("driverClass", "org.resthub.TestDriverClass");
        configProps.put("acquireIncrement", "1");
        configProps.put("acquireRetryAttempts", "2");
        configProps.put("acquireRetryDelayInMs", "5000");
        configProps.put("closeConnectionWatchTimeoutInMs", "1");
        configProps.put("connectionTimeoutInMs", "1");
        configProps.put("connectionHookClassName", "org.resthub.Test");
        configProps.put("connectionTestStatement", "select 1");
        configProps.put("defaultAutoCommit", "false");
        configProps.put("defaultCatalog", "resthubCatalog");
        configProps.put("defaultReadOnly", "true");
        configProps.put("defaultTransactionIsolation", "default");
        configProps.put("idleConnectionTestPeriodInMinutes", "50");
        configProps.put("idleMaxAgeInMinutes", "50");
        configProps.put("initSQL", "select 1");
        configProps.put("poolName", "ResthubPool");
        configProps.put("maxConnectionAgeInSeconds", "10");
        configProps.put("maxConnectionsPerPartition", "3");
        configProps.put("minConnectionsPerPartition", "2");
        configProps.put("partitionCount", "2");
        configProps.put("poolAvailabilityThreshold", "1");
        configProps.put("poolStrategy", "CACHED");
        configProps.put("queryExecuteTimeLimitInMs", "10");
        configProps.put("serviceOrder", "LIFO");
        configProps.put("statementsCacheSize", "1");
        configProps.put("jdbcUrl", "url");
        configProps.put("username", "user");
        configProps.put("password", "pass");

        DataSource dataSource = boneCPDataSourceFactory.create(BoneCPTestDataSource.class, configProps);
        Assertions.assertThat(dataSource).isNotNull().isInstanceOf(BoneCPTestDataSource.class);

        BoneCPTestDataSource testDataSource = (BoneCPTestDataSource) dataSource;
        Assertions.assertThat(testDataSource).isNotNull();
        Assertions.assertThat(testDataSource.getDriverClass()).isNotNull().isEqualTo(configProps.getProperty("driverClass"));

        Assertions.assertThat(testDataSource.getConfig()).isNotNull();

        // check that BoneCP defaults are kept
        Assertions.assertThat(testDataSource.getConfig().getAcquireIncrement()).isEqualTo(new Integer(configProps.getProperty("acquireIncrement")));
        Assertions.assertThat(testDataSource.getConfig().getAcquireRetryAttempts()).isEqualTo(new Integer(configProps.getProperty("acquireRetryAttempts")));
        Assertions.assertThat(testDataSource.getConfig().getAcquireRetryDelayInMs()).isEqualTo(new Integer(configProps.getProperty("acquireRetryDelayInMs")));
        Assertions.assertThat(testDataSource.getConfig().getCloseConnectionWatchTimeoutInMs()).isEqualTo(new Integer(configProps.getProperty("closeConnectionWatchTimeoutInMs")));
        Assertions.assertThat(testDataSource.getConfig().getConnectionTimeoutInMs()).isEqualTo(new Integer(configProps.getProperty("connectionTimeoutInMs")));
        Assertions.assertThat(testDataSource.getConfig().getConnectionHookClassName()).isEqualTo(configProps.getProperty("connectionHookClassName"));
        Assertions.assertThat(testDataSource.getConfig().getConnectionTestStatement()).isEqualTo(configProps.getProperty("connectionTestStatement"));
        Assertions.assertThat(testDataSource.getConfig().getDefaultAutoCommit()).isEqualTo(Boolean.getBoolean(configProps.getProperty("defaultAutoCommit")));
        Assertions.assertThat(testDataSource.getConfig().getDefaultCatalog()).isEqualTo(configProps.getProperty("defaultCatalog"));
        Assertions.assertThat(testDataSource.getConfig().getDefaultReadOnly()).isEqualTo(Boolean.getBoolean(configProps.getProperty("defaultReadOnly")));
        Assertions.assertThat(testDataSource.getConfig().getDefaultTransactionIsolation()).isEqualTo(configProps.getProperty("defaultTransactionIsolation"));
        Assertions.assertThat(testDataSource.getConfig().getIdleConnectionTestPeriodInMinutes()).isEqualTo(new Integer(configProps.getProperty("idleConnectionTestPeriodInMinutes")));
        Assertions.assertThat(testDataSource.getConfig().getIdleMaxAgeInMinutes()).isEqualTo(new Integer(configProps.getProperty("idleMaxAgeInMinutes")));
        Assertions.assertThat(testDataSource.getConfig().getInitSQL()).isEqualTo(configProps.getProperty("initSQL"));
        Assertions.assertThat(testDataSource.getConfig().getPoolName()).isEqualTo(configProps.getProperty("poolName"));
        Assertions.assertThat(testDataSource.getConfig().getMaxConnectionAgeInSeconds()).isEqualTo(new Integer(configProps.getProperty("maxConnectionAgeInSeconds")));
        Assertions.assertThat(testDataSource.getConfig().getMaxConnectionsPerPartition()).isEqualTo(new Integer(configProps.getProperty("maxConnectionsPerPartition")));
        Assertions.assertThat(testDataSource.getConfig().getMinConnectionsPerPartition()).isEqualTo(new Integer(configProps.getProperty("minConnectionsPerPartition")));
        Assertions.assertThat(testDataSource.getConfig().getPartitionCount()).isEqualTo(new Integer(configProps.getProperty("partitionCount")));
        Assertions.assertThat(testDataSource.getConfig().getPoolAvailabilityThreshold()).isEqualTo(new Integer(configProps.getProperty("poolAvailabilityThreshold")));
        Assertions.assertThat(testDataSource.getConfig().getPoolStrategy()).isEqualTo(configProps.getProperty("poolStrategy"));
        Assertions.assertThat(testDataSource.getConfig().getQueryExecuteTimeLimitInMs()).isEqualTo(new Integer(configProps.getProperty("queryExecuteTimeLimitInMs")));
        Assertions.assertThat(testDataSource.getConfig().getServiceOrder()).isEqualTo(configProps.getProperty("serviceOrder"));
        Assertions.assertThat(testDataSource.getConfig().getStatementsCacheSize()).isEqualTo(new Integer(configProps.getProperty("statementsCacheSize")));
        Assertions.assertThat(testDataSource.getConfig().getJdbcUrl()).isEqualTo(configProps.getProperty("jdbcUrl"));
        Assertions.assertThat(testDataSource.getConfig().getUsername()).isEqualTo(configProps.getProperty("username"));
        Assertions.assertThat(testDataSource.getConfig().getPassword()).isEqualTo(configProps.getProperty("password"));
    }


    @Test
    public void testBoneCPDataSourceConfigPartialAnUnresolvedProps() throws Exception {

        Properties configProps = new Properties();
        configProps.put("driverClass", "org.resthub.TestDriverClass");
        configProps.put("jdbcUrl", "url");
        configProps.put("username", "user");
        configProps.put("password", "pass");
        configProps.put("acquireIncrement", "1");
        configProps.put("poolName", "retshubPoolName");
        configProps.put("maxConnectionsPerPartition", "${maxConnectionsPerPartition}");
        configProps.put("poolStrategy", "${poolStrategy}");
        configProps.put("idleMaxAgeInMinutes", "${idleMaxAgeInMinutes}");

        DataSource dataSource = boneCPDataSourceFactory.create(BoneCPTestDataSource.class, configProps);
        Assertions.assertThat(dataSource).isNotNull().isInstanceOf(BoneCPTestDataSource.class);

        BoneCPTestDataSource testDataSource = (BoneCPTestDataSource) dataSource;
        Assertions.assertThat(testDataSource).isNotNull();
        Assertions.assertThat(testDataSource.getDriverClass()).isNotNull().isEqualTo(configProps.getProperty("driverClass"));

        Assertions.assertThat(testDataSource.getConfig()).isNotNull();

        // check that BoneCP defaults are kept
        BoneCPConfig expectedDefaults = new BoneCPConfig();
        Assertions.assertThat(testDataSource.getConfig().getAcquireRetryAttempts()).isEqualTo(expectedDefaults.getAcquireRetryAttempts());
        Assertions.assertThat(testDataSource.getConfig().getAcquireRetryDelayInMs()).isEqualTo(expectedDefaults.getAcquireRetryDelayInMs());
        Assertions.assertThat(testDataSource.getConfig().getCloseConnectionWatchTimeoutInMs()).isEqualTo(expectedDefaults.getCloseConnectionWatchTimeoutInMs());
        Assertions.assertThat(testDataSource.getConfig().getConnectionTimeoutInMs()).isEqualTo(expectedDefaults.getConnectionTimeoutInMs());
        Assertions.assertThat(testDataSource.getConfig().getConnectionHookClassName()).isEqualTo(expectedDefaults.getConnectionHookClassName());
        Assertions.assertThat(testDataSource.getConfig().getConnectionTestStatement()).isEqualTo(expectedDefaults.getConnectionTestStatement());
        Assertions.assertThat(testDataSource.getConfig().getDefaultAutoCommit()).isEqualTo(expectedDefaults.getDefaultAutoCommit());
        Assertions.assertThat(testDataSource.getConfig().getDefaultCatalog()).isEqualTo(expectedDefaults.getDefaultCatalog());
        Assertions.assertThat(testDataSource.getConfig().getDefaultReadOnly()).isEqualTo(expectedDefaults.getDefaultReadOnly());
        Assertions.assertThat(testDataSource.getConfig().getDefaultTransactionIsolation()).isEqualTo(expectedDefaults.getDefaultTransactionIsolation());
        Assertions.assertThat(testDataSource.getConfig().getIdleConnectionTestPeriodInMinutes()).isEqualTo(expectedDefaults.getIdleConnectionTestPeriodInMinutes());
        Assertions.assertThat(testDataSource.getConfig().getIdleMaxAgeInMinutes()).isEqualTo(expectedDefaults.getIdleMaxAgeInMinutes());
        Assertions.assertThat(testDataSource.getConfig().getInitSQL()).isEqualTo(expectedDefaults.getInitSQL());
        Assertions.assertThat(testDataSource.getConfig().getMaxConnectionAgeInSeconds()).isEqualTo(expectedDefaults.getMaxConnectionAgeInSeconds());
        Assertions.assertThat(testDataSource.getConfig().getMaxConnectionsPerPartition()).isEqualTo(expectedDefaults.getMaxConnectionsPerPartition());
        Assertions.assertThat(testDataSource.getConfig().getMinConnectionsPerPartition()).isEqualTo(expectedDefaults.getMinConnectionsPerPartition());
        Assertions.assertThat(testDataSource.getConfig().getPartitionCount()).isEqualTo(expectedDefaults.getPartitionCount());
        Assertions.assertThat(testDataSource.getConfig().getPoolAvailabilityThreshold()).isEqualTo(expectedDefaults.getPoolAvailabilityThreshold());
        Assertions.assertThat(testDataSource.getConfig().getPoolStrategy()).isEqualTo(expectedDefaults.getPoolStrategy());
        Assertions.assertThat(testDataSource.getConfig().getQueryExecuteTimeLimitInMs()).isEqualTo(expectedDefaults.getQueryExecuteTimeLimitInMs());
        Assertions.assertThat(testDataSource.getConfig().getServiceOrder()).isEqualTo(expectedDefaults.getServiceOrder());
        Assertions.assertThat(testDataSource.getConfig().getStatementsCacheSize()).isEqualTo(expectedDefaults.getStatementsCacheSize());

        // check concrete datasource parameters
        Assertions.assertThat(testDataSource.getConfig().getJdbcUrl()).isEqualTo(configProps.getProperty("jdbcUrl"));
        Assertions.assertThat(testDataSource.getConfig().getUsername()).isEqualTo(configProps.getProperty("username"));
        Assertions.assertThat(testDataSource.getConfig().getPassword()).isEqualTo(configProps.getProperty("password"));
        Assertions.assertThat(testDataSource.getConfig().getPoolName()).isEqualTo(configProps.getProperty("poolName"));
        Assertions.assertThat(testDataSource.getConfig().getAcquireIncrement()).isEqualTo(new Integer(configProps.getProperty("acquireIncrement")));
    }

}
