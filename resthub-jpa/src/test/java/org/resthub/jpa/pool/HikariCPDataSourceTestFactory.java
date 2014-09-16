package org.resthub.jpa.pool;

import com.zaxxer.hikari.HikariCPTestDataSource;

import javax.sql.DataSource;
import java.util.Properties;

public class HikariCPDataSourceTestFactory extends HikariCPDataSourceFactory {

    private String originalDataSourceClassName;

    public HikariCPDataSourceTestFactory() {
        this.setDsClass(HikariCPTestDataSource.class);
    }

    @Override
    public DataSource create(Properties dsProperties)
            throws Exception {

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
