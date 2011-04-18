package org.resthub.test.dbunit.initializer;

import java.sql.Connection;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.util.Assert;

/**
 * A {@link DatabaseInitializer} implementation based on Spring
 * {@link DatabasePopulator}, to perform initialization using SQL scripts.
 * 
 * @author vanackej
 * 
 */
public class DatabasePopulatorDatabaseInitializer implements
		DatabaseInitializer {

	private DataSource dataSource;

	private DatabasePopulator databasePopulator;

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public DatabasePopulator getDatabasePopulator() {
		return databasePopulator;
	}

	public void setDatabasePopulator(DatabasePopulator databasePopulator) {
		this.databasePopulator = databasePopulator;
	}

	@Override
	public void initDatabase() throws Exception {

		Connection connection = DataSourceUtils.getConnection(dataSource);
		try {
			databasePopulator.populate(connection);
		} finally {
			DataSourceUtils.releaseConnection(connection, dataSource);
		}

	}

	@PostConstruct
	public void init() throws Exception {
		Assert.notNull(dataSource, "A dataSource is required");
		Assert.notNull(databasePopulator, "A databasePopulator is required");
	}

}
