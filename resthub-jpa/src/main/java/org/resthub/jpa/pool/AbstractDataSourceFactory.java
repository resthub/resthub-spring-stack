package org.resthub.jpa.pool;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

/**
 * Common abstract factory for dataSource creation
 * <p/>
 * This factory allows to create directly a DataSource instance from a set of configuration
 * properties.
 * <p/>
 * It provides complementary operations on properties to check if property has been resolved and ignore it
 * if it was not. This behaviour allows, for instance, to expose entire dataSource options to end user through spring
 * placeholder without necessity to override all defaults from dataSource.
 */
public abstract class AbstractDataSourceFactory<T extends DataSource> {

    private Class<? extends T> dsClass;

    public DataSource create(Class<? extends T> clazz, Properties dsProperties)
            throws Exception {
        this.dsClass = clazz;
        return this.create(dsProperties);
    }


    /**
     * Create new instance of {@link DataSource} fully configured with properties.
     * <p/>
     * All given properties will be set configured but the process ignore unresolved properties that will
     * the be set to DataSource default values.
     *
     * @param dsProperties configuration properties
     * @return the new DataSource instance.
     */
    public DataSource create(Properties dsProperties)
            throws Exception {
        Properties configProperties = new Properties();

        if (dsProperties != null) {

            for (Object o : dsProperties.keySet()) {
                String key = (String) o;
                if (isResolvedProperty(dsProperties, key)) {
                    configProperties.put(key, dsProperties.get(key));
                }
            }
        }

        return instantiateDataSource(configProperties);
    }

    /**
     * Check if a property has been resolved. i.e. the property does not contain the
     * special spring placeholder resolver character '$').
     *
     * @param dsProperties properties map containing property to check
     * @param key          key identifier of the property to check
     * @return true if the property has been resolved, false otherwise
     */
    protected boolean isResolvedProperty(Properties dsProperties, String key) {
        return !(dsProperties.get(key) != null
                && dsProperties.get(key) instanceof String)
                || !((String) dsProperties.get(key)).startsWith("$");

    }

    /**
     * Perform call to the {@link javax.sql.DataSource} constructor.
     * <p/>
     * This method calls by default no-args constructor but can be overridden to use a constructor
     * with args instead.
     *
     * @param clazz            class to instantiate. Cannot be null
     * @param configProperties properties list to configure new {@link javax.sql.DataSource}. by default, these
     *                         properties are not used because default no-arg constructor is called but are
     *                         available in case of specific override. Cannot be null
     * @return the new created {@link javax.sql.DataSource} instance
     * @throws NoSuchMethodException     if the specified constructor was not found
     * @throws IllegalAccessException    if the specified constructor cannot be called due to access restrictions
     * @throws InvocationTargetException if the underlying constructor throws an exception.
     * @throws InstantiationException    if the class that declares the underlying constructor represents an abstract class
     */
    protected DataSource callConstructor(Class<? extends T> clazz, Properties configProperties)
            throws Exception {
        return clazz.getConstructor().newInstance();
    }

    /**
     * Instantiate a concrete dataSource instance depending on the
     * {@link #dsClass property} value
     *
     * @param configProperties configuration properties. Cannot be null
     * @return the new created and configured {@link javax.sql.DataSource} instance
     */
    protected DataSource instantiateDataSource(Properties configProperties)
            throws Exception {
        DataSource dataSource = null;

        if (this.dsClass != null) {
            dataSource = this.callConstructor(this.dsClass, configProperties);
        }

        return dataSource;
    }

    public Class<? extends T> getDsClass() {
        return dsClass;
    }

    public void setDsClass(Class<? extends T> dsClass) {
        this.dsClass = dsClass;
    }
}
