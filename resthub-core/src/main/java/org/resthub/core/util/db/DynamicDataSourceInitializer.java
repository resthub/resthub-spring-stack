package org.resthub.core.util.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.util.Assert;

public class DynamicDataSourceInitializer implements InitializingBean {

	private Map<String, DatabasePopulator> exceptions = new HashMap<String, DatabasePopulator>();

	private DataSource dataSource;

	private DatabasePopulator databasePopulator;

	private boolean enabled = true;

	/**
	 * The {@link DataSource} to populate when this component is initialized.
	 * Mandatory with no default.
	 * 
	 * @param dataSource
	 *            the DataSource
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * The {@link DatabasePopulator} to use to populate the data source.
	 * Mandatory with no default.
	 * 
	 * @param databasePopulator
	 *            the database populator to use.
	 */
	public void setDatabasePopulator(DatabasePopulator databasePopulator) {
		this.databasePopulator = databasePopulator;
	}

	/**
	 * Flag to explicitly enable or disable the database populator.
	 * 
	 * @param enabled
	 *            true if the database populator will be called on startup
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setExceptions(Map<String, DatabasePopulator> exceptions) {
		this.exceptions = exceptions;
	}
	
	public Map<String, DatabasePopulator> getExceptions() {
		return exceptions;
	}

	/**
	 * Use the populator to set up data in the data source.
	 */
	public void afterPropertiesSet() throws Exception {
		if (this.enabled) {
			Assert.state(this.dataSource != null, "DataSource must be provided");
			Assert.state(this.databasePopulator != null,
					"DatabasePopulator must be provided");
			DatabasePopulator actualDatabasePopulator = this.databasePopulator;
			if (exceptions != null) {
				DatabaseDescriptor descriptor = new DatabaseDescriptor(this.dataSource);
				if (exceptions.containsKey(descriptor.getProductName())) {
					actualDatabasePopulator = exceptions.get(descriptor.getProductName());
				}
			}
			
			try {
				Connection connection = DataSourceUtils.getConnection(this.dataSource);
				try {
					actualDatabasePopulator.populate(connection);
				} finally {
					DataSourceUtils.releaseConnection(connection, this.dataSource);
				}
			} catch (Exception ex) {
				throw new DataAccessResourceFailureException(
						"Failed to populate database", ex);
			}
		}
	}

}
