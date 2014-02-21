package org.resthub.jpa.pool;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariTestDataSource;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

public class HikariDataSourceTestFactory extends HikariDataSourceFactory {

    private String originalDataSourceClassName;

    public HikariDataSourceTestFactory() {
        this.setDsClass(HikariTestDataSource.class);
    }

    @Override
    public DataSource create(Properties dsProperties)
            throws ClassNotFoundException, NoSuchMethodException, InstantiationException,
            IllegalAccessException, InvocationTargetException {

        this.setOriginalDataSourceClassName(dsProperties.getProperty("dataSourceClassName"));
        dsProperties.put("dataSourceClassName", "org.resthub.jpa.sql.FakeDataSource");
        return super.create(dsProperties);
    }

    public String getOriginalDataSourceClassName() {
        return originalDataSourceClassName;
    }

    public void setOriginalDataSourceClassName(String originalDataSourceClassName) {
        this.originalDataSourceClassName = originalDataSourceClassName;
    }
}
