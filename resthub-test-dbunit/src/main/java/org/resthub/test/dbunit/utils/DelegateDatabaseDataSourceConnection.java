package org.resthub.test.dbunit.utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.statement.IStatementFactory;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.ext.h2.H2Connection;
import org.dbunit.ext.hsqldb.HsqldbConnection;
import org.dbunit.ext.mysql.MySqlConnection;
import org.dbunit.ext.oracle.OracleConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DelegateDatabaseDataSourceConnection implements
		IDatabaseConnection {

	private Logger logger = LoggerFactory
			.getLogger(DelegateDatabaseDataSourceConnection.class);

	private DatabaseConnection databaseConnection;

	private String disableFkCommand = null;
	private String enableFkCommand = null;

	private Connection connection;

	private DataSource dataSource;

	public DelegateDatabaseDataSourceConnection(DataSource dataSource)
			throws SQLException, DatabaseUnitException {
		this(dataSource, null);
	}

	public DelegateDatabaseDataSourceConnection(DataSource dataSource,
			String schema) throws SQLException, DatabaseUnitException {
		this.dataSource = dataSource;
		Connection connection = dataSource.getConnection();
		DatabaseMetaData metatData = connection.getMetaData();
		String productName = metatData.getDatabaseProductName().toLowerCase();

		if (productName.contains("hsql")) {
			logger.info("Detected database : hsql");
			databaseConnection = new HsqldbConnection(connection, null);
			disableFkCommand = "SET DATABASE REFERENTIAL INTEGRITY FALSE;";
			enableFkCommand = "SET DATABASE REFERENTIAL INTEGRITY TRUE;";
		} else if (productName.contains("mysql")) {
			logger.info("Detected database : mysql");
			databaseConnection = new MySqlConnection(connection, null);
			disableFkCommand = "SET foreign_key_checks = 0;";
			enableFkCommand = "SET foreign_key_checks = 1;";
		} else if (productName.contains("oracle")) {
			logger.info("Detected database : oracle");
			databaseConnection = new OracleConnection(connection, null);
			// TODO disable FK for oracle
		} else if (productName.contains("h2")) {
			logger.info("Detected database : h2");
			databaseConnection = new H2Connection(connection, null);
			disableFkCommand = "SET REFERENTIAL_INTEGRITY FALSE";
			enableFkCommand = "SET REFERENTIAL_INTEGRITY TRUE";
		} else {
			logger.info("No specific database detected, using default");
			databaseConnection = new DatabaseConnection(connection, null);
		}
		databaseConnection.getConfig().setProperty(
				DatabaseConfig.FEATURE_BATCHED_STATEMENTS, true);
	}

	public Connection getConnection() throws SQLException {
		if (connection == null) {
			connection = dataSource.getConnection();
			if (disableFkCommand != null) {
				Statement statement = connection.createStatement();
				try {
					statement.execute(disableFkCommand);
				} finally {
					statement.close();
				}
			}
		}
		return connection;
	}

	public void close() throws SQLException {
		if (connection != null) {
			if (enableFkCommand != null) {
				Statement statement = connection.createStatement();
				try {
					statement.execute(enableFkCommand);
				} finally {
					statement.close();
				}
			}
			connection.close();
			connection = null;
		}
	}

	public String getSchema() {
		return databaseConnection.getSchema();
	}

	public IDataSet createDataSet() throws SQLException {
		return databaseConnection.createDataSet();
	}

	public IDataSet createDataSet(String[] tableNames) throws DataSetException,
			SQLException {
		return databaseConnection.createDataSet(tableNames);
	}

	public ITable createQueryTable(String resultName, String sql)
			throws DataSetException, SQLException {
		return databaseConnection.createQueryTable(resultName, sql);
	}

	public ITable createTable(String resultName,
			PreparedStatement preparedStatement) throws DataSetException,
			SQLException {
		return databaseConnection.createTable(resultName, preparedStatement);
	}

	public ITable createTable(String tableName) throws DataSetException,
			SQLException {
		return databaseConnection.createTable(tableName);
	}

	public boolean equals(Object obj) {
		return databaseConnection.equals(obj);
	}

	public DatabaseConfig getConfig() {
		return databaseConnection.getConfig();
	}

	public int getRowCount(String tableName, String whereClause)
			throws SQLException {
		return databaseConnection.getRowCount(tableName, whereClause);
	}

	public int getRowCount(String tableName) throws SQLException {
		return databaseConnection.getRowCount(tableName);
	}

	@SuppressWarnings("deprecation")
	public IStatementFactory getStatementFactory() {
		return databaseConnection.getStatementFactory();
	}

	public int hashCode() {
		return databaseConnection.hashCode();
	}

	public String toString() {
		return databaseConnection.toString();
	}

}
