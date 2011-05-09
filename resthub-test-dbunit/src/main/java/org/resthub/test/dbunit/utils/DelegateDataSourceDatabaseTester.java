package org.resthub.test.dbunit.utils;

import javax.sql.DataSource;

import org.dbunit.AbstractDatabaseTester;
import org.dbunit.database.IDatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DelegateDataSourceDatabaseTester extends AbstractDatabaseTester {

	private static final Logger logger = LoggerFactory
			.getLogger(DelegateDataSourceDatabaseTester.class);

	private DataSource dataSource;

	/**
	 * Creates a new DelegateDataSourceDatabaseTester with the specified
	 * DataSource.
	 * 
	 * @param dataSource
	 *            the DataSource to pull connections from
	 */
	public DelegateDataSourceDatabaseTester(DataSource dataSource) {
		super();

		if (dataSource == null) {
			throw new NullPointerException(
					"The parameter 'dataSource' must not be null");
		}
		this.dataSource = dataSource;
	}

	/**
	 * Creates a new DelegateDataSourceDatabaseTester with the specified
	 * DataSource and schema name.
	 * 
	 * @param dataSource
	 *            the DataSource to pull connections from
	 * @param schema
	 *            The schema name to be used for new dbunit connections
	 * @since 2.4.5
	 */
	public DelegateDataSourceDatabaseTester(DataSource dataSource, String schema) {
		super(schema);
		assertTrue("DataSource is not set", dataSource != null);

		if (dataSource == null) {
			throw new NullPointerException(
					"The parameter 'dataSource' must not be null");
		}
		this.dataSource = dataSource;
	}

	public IDatabaseConnection getConnection() throws Exception {
		logger.debug("getConnection() - start");

		return new DelegateDatabaseDataSourceConnection(dataSource, getSchema());
	}

}
