package org.resthub.test.dbunit.config;

import java.util.List;

import org.dbunit.IDatabaseTester;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.filter.ExcludeTableFilter;
import org.dbunit.dataset.filter.IncludeTableFilter;

/**
 * Stores DBunit configuration
 * 
 * @author vanackej
 * 
 */
public class DbUnitConfiguration {

	private IDatabaseConnection databaseConnection;
	
	private IDatabaseTester databaseTester;

	private List<String> excludeTables;

	private List<String> includeTables;

	public IDatabaseConnection getDatabaseConnection() {
		return databaseConnection;
	}

	public void setDatabaseConnection(IDatabaseConnection databaseConnection) {
		this.databaseConnection = databaseConnection;
	}

	public IDatabaseTester getDatabaseTester() {
		return databaseTester;
	}

	public void setDatabaseTester(IDatabaseTester databaseTester) {
		this.databaseTester = databaseTester;
	}

	public List<String> getExcludeTables() {
		return excludeTables;
	}

	/**
	 * Set the table list to exclude during dataset injection or extraction.
	 * Accepted values are either a table name or a pattern including * and ?
	 * characters.
	 * 
	 * @see ExcludeTableFilter
	 */
	public void setExcludeTables(List<String> excludeTables) {
		this.excludeTables = excludeTables;
	}

	public List<String> getIncludeTables() {
		return includeTables;
	}

	/**
	 * Set the table list to include during dataset injection or extraction.
	 * Accepted values are either a table name or a pattern including * and ?
	 * characters.
	 * 
	 * @see IncludeTableFilter
	 */
	public void setIncludeTables(List<String> includeTables) {
		this.includeTables = includeTables;
	}

}
