package org.resthub.jpa;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.resthub.jpa.DatabaseDescriptor;
import org.springframework.beans.factory.FactoryBean;

/**
 * <p>
 * Spring {@link FactoryBean} bean, to be used to create a
 * {@link org.resthub.jpa.DatabaseDescriptor} from a {@link DataSource}.
 * </p>
 * <p>
 * Usage :
 * </p>
 * 
 * <pre>
 * {@code
 * <bean id="databaseDescriptor" class="org.resthub.jpa.util.db.DatabaseDescriptorFactory">
 *   <property name="dataSource" ref="dataSource" />
 * </bean>
 * }
 * </pre>
 * 
 * <p>
 * Or with resthub namespace support
 * </p>
 */
public class DatabaseDescriptorFactory implements FactoryBean<DatabaseDescriptor> {

    private DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DatabaseDescriptor getObject() throws SQLException {
        return new DatabaseDescriptor(dataSource);
    }

    public Class<?> getObjectType() {
        return DatabaseDescriptor.class;
    }

    public boolean isSingleton() {
        return true;
    }

}
