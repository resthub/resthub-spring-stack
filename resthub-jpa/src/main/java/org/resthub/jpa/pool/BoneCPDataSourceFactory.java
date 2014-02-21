package org.resthub.jpa.pool;

import com.jolbox.bonecp.BoneCPConfig;
import com.jolbox.bonecp.BoneCPDataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

/**
 * This factory allows to create directly a BoneCPDataSource instance from a set of configuration
 * properties.
 * <p/>
 * Check <a href="http://jolbox.com/index.html?page=http://jolbox.com/configuration.html">BoneCP documentation</a>
 * for a full list of configuration properties.
 * <p/>
 * @see AbstractDataSourceFactory#create(java.util.Properties)
 * @see AbstractDataSourceFactory
 */
public class BoneCPDataSourceFactory extends AbstractDataSourceFactory<BoneCPDataSource> {

    public BoneCPDataSourceFactory() {
        this.setDsClass(BoneCPDataSource.class);
    }

    /**
     * Initialize a new {@link com.jolbox.bonecp.BoneCPDataSource} instance with given configuration
     * properties
     *
     * @see AbstractDataSourceFactory#callConstructor(Class, java.util.Properties)
     */
    @Override
    protected DataSource callConstructor(Class clazz, Properties configProperties) throws Exception {
        BoneCPConfig config = new BoneCPConfig(configProperties);
        BoneCPDataSource dataSource = (BoneCPDataSource) clazz.getConstructor(BoneCPConfig.class).newInstance(config);
        dataSource.setDriverClass(configProperties.getProperty("driverClass"));
        return dataSource;
    }
}
