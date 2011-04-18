package org.resthub.test.dbunit.config;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.filter.ExcludeTableFilter;
import org.dbunit.dataset.filter.IncludeTableFilter;
import org.resthub.test.dbunit.utils.DelegateDatabaseDataSourceConnection;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * @author vanackej
 * 
 */
public class DbUnitConfiguration implements InitializingBean {

	private IDatabaseConnection databaseConnection;

	private List<String> excludeTables;

	private List<String> includeTables;

	private DataSource dataSource;

	private Map<String, Object> dbUnitProperties;

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

	public IDatabaseConnection getDatabaseConnection() {
		return databaseConnection;
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

	public void afterPropertiesSet() throws Exception {

		// Initialize only if no databaseConnection has been injected (legacy
		// initialization mode)
		Assert.notNull(dataSource, "dataSource is required");

		databaseConnection = new DelegateDatabaseDataSourceConnection(
				dataSource, true);

		if (dbUnitProperties != null && !dbUnitProperties.isEmpty()) {
			for (Map.Entry<String, Object> entry : dbUnitProperties.entrySet()) {
				databaseConnection.getConfig().setProperty(entry.getKey(),
						entry.getValue());
			}
		}

	}

}
