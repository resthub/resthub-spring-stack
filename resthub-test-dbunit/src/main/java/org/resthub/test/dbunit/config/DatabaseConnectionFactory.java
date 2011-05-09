package org.resthub.test.dbunit.config;

import java.util.Map;

import javax.sql.DataSource;

import org.dbunit.database.IDatabaseConnection;
import org.resthub.test.dbunit.utils.DelegateDatabaseDataSourceConnection;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.util.Assert;

/**
 * Factory bean for {@link IDatabaseConnection}. It creates an instance of {@link DelegateDatabaseDataSourceConnection}
 * to perform automatic configuration depending on the detected database.
 * 
 * @author vanackej
 *
 */
public class DatabaseConnectionFactory implements FactoryBean<IDatabaseConnection> {

	private Map<String, Object> dbUnitProperties;
	
	private DataSource dataSource;
	
	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public Map<String, Object> getDbUnitProperties() {
		return dbUnitProperties;
	}

	/**
	 * Optionaly set the DBUnit properties to use. see <a
	 * href="http://www.dbunit.org/properties.html">dunit documentation</a> for
	 * more information about valid properties.
	 * 
	 * @param dbUnitProperties
	 */
	public void setDbUnitProperties(Map<String, Object> dbUnitProperties) {
		this.dbUnitProperties = dbUnitProperties;
	}
	
	@Override
	public IDatabaseConnection getObject() throws Exception {
		Assert.notNull(dataSource, "dataSource is required");

		DelegateDatabaseDataSourceConnection databaseConnection = new DelegateDatabaseDataSourceConnection(
				dataSource);

		if (dbUnitProperties != null && !dbUnitProperties.isEmpty()) {
			for (Map.Entry<String, Object> entry : dbUnitProperties.entrySet()) {
				databaseConnection.getConfig().setProperty(entry.getKey(),
						entry.getValue());
			}
		}
		return databaseConnection;
	}

	@Override
	public Class<?> getObjectType() {
		return IDatabaseConnection.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
