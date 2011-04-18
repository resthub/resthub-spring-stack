package org.resthub.test.dbunit.utils;

import javax.sql.DataSource;

import org.dbunit.DataSourceDatabaseTester;
import org.dbunit.database.IDatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DelegateDataSourceDatabaseTester extends DataSourceDatabaseTester {
	
	private static final Logger logger = LoggerFactory.getLogger(DelegateDataSourceDatabaseTester.class);

	private DataSource dataSource; 
	
	public DelegateDataSourceDatabaseTester(DataSource dataSource) {
		super(dataSource);
		this.dataSource = dataSource;
	}

	@Override
	public IDatabaseConnection getConnection() throws Exception {
		logger.debug("getConnection() - start");

		assertTrue( "DataSource is not set", dataSource!=null );
		return new DelegateDatabaseDataSourceConnection(dataSource, true);
	}

	
	
}
