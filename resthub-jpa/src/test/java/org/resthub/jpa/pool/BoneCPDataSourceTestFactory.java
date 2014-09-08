package org.resthub.jpa.pool;

import com.jolbox.bonecp.BoneCPTestDataSource;

import javax.sql.DataSource;
import java.util.Properties;

public class BoneCPDataSourceTestFactory extends BoneCPDataSourceFactory {

    private String originalDataSourceClassName;

    public BoneCPDataSourceTestFactory() {
        this.setDsClass(BoneCPTestDataSource.class);
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
