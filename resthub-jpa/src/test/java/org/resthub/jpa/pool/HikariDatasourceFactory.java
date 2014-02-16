package org.resthub.jpa.pool;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.util.Iterator;
import java.util.Properties;

/**
 * This factory allows to create directly a HikariDataSource instance from a set of configuration
 * properties.
 *
 * It remove necessity to provide additional hikariConfig spring bean to pass as constructor arg of
 * HikaridataSource.
 *
 * It provides also complementary operations on properties to check if property has been resolved and ignore it
 * if it was not. This behaviour allows, for instance, to expose entire hikari options to end user through spring
 * placeholder without necessity to override all defaults from Hikari.
 *
 */
public class HikariDataSourceFactory {

    /**
     * Create new instance of HikariDataSource fully configured with properties.
     *
     * All given properties will be set configured but the process ignore unresolved properties that will
     * the be set to HikariCP default values.
     *
     * Check <a href="https://github.com/brettwooldridge/HikariCP#configuration-knobs-baby">Hikari documentation</a>
     * for a full list of configuration properties.
     *
     * Be aware that some specific datasource properties must be prefixed by 'dataSource.'. cf
     * <a href="https://github.com/brettwooldridge/HikariCP#initialization">Hikari datasource initialization</a>
     *
     * @param dsProperties configuration properties
     * @return the new HikariDataSource instance.
     */
    public HikariDataSource create(Properties dsProperties) {
        Properties configProperties = new Properties();
        Iterator it = dsProperties.keySet().iterator();

        while (it.hasNext()) {
            String key = (String) it.next();
            if (isResolvedProperty(dsProperties, key)) {
                configProperties.put(key, dsProperties.get(key));
            }
        }

        HikariConfig config = new HikariConfig(configProperties);
        return new HikariDataSource(config);
    }

    /**
     * Check if a property has been resolved. i.e. the property does not contain the
     * special spring placeholder resolver character '$').
     *
     * @param dsProperties properties map containing property to check
     * @param key key identifier of the property to check
     * @return true if the property has been resolved, false otherwise
     */
    protected boolean isResolvedProperty(Properties dsProperties, String key) {
        return !((String) dsProperties.get(key)).startsWith("$");
    }
}
