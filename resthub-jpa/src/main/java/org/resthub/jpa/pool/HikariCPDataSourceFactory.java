package org.resthub.jpa.pool;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

/**
 * This factory allows to create directly a HikariDataSource instance from a set of configuration
 * properties.
 * <p/>
 * It removes necessity to provide additional hikariConfig spring bean to pass as constructor arg of
 * HikaridataSource.
 * <p/>
 * Check <a href="https://github.com/brettwooldridge/HikariCP#configuration-knobs-baby">Hikari documentation</a>
 * for a full list of configuration properties.
 * <p/>
 * Be aware that some specific datasource properties must be prefixed by 'dataSource.'. cf
 * <a href="https://github.com/brettwooldridge/HikariCP#initialization">Hikari datasource initialization</a>
 *
 * @see org.resthub.jpa.pool.AbstractDataSourceFactory#create(java.util.Properties)
 * @see org.resthub.jpa.pool.AbstractDataSourceFactory
 */
public class HikariCPDataSourceFactory extends AbstractDataSourceFactory<HikariDataSource> {

    public HikariCPDataSourceFactory() {
        this.setDsClass(HikariDataSource.class);
    }

    /**
     * Initialize a new {@link com.zaxxer.hikari.HikariDataSource} instance with given configuration
     * properties
     *
     * @see org.resthub.jpa.pool.AbstractDataSourceFactory#callConstructor(Class, java.util.Properties)
     */
    @Override
    protected DataSource callConstructor(Class clazz, Properties configProperties) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        HikariConfig config = new HikariConfig(configProperties);
        return (DataSource) clazz.getConstructor(HikariConfig.class).newInstance(config);
    }
}
