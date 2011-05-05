package org.resthub.test.dbunit.resolver;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.resthub.test.dbunit.annotation.DBOperation;
import org.resthub.test.dbunit.config.DbUnitConfiguration;
import org.resthub.test.dbunit.initializer.DatabaseInitializer;
import org.resthub.test.dbunit.utils.DBUnitUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link DataSetResolver} who resolves datasets using {@link DatabaseInitializer} 
 * 
 * @author vanackej
 *
 */
public class InitializerDataSetResolver implements DataSetResolver {

	private Logger logger = LoggerFactory.getLogger(InitializerDataSetResolver.class);

	/**
	 * Cache for initialized datasets, ready to inject again. 
	 */
	private Map<String, IDataSet> cachedDataSets = new HashMap<String, IDataSet>();
	
	private Map<String,DatabaseInitializer> initializers;
	
	private DbUnitConfiguration configuration;
	
	public InitializerDataSetResolver(DbUnitConfiguration configuration, Map<String, DatabaseInitializer> initializers) {
		this.configuration = configuration;
		this.initializers = initializers;
	}

	public IDataSet resolveDataSet(String dataSetName) throws Exception {
		if (!cachedDataSets.containsKey(dataSetName)) {
			if (initializers != null && initializers.containsKey(dataSetName)) {
				IDataSet dataSet = DBUnitUtils.filter(configuration.getDatabaseConnection().createDataSet(), configuration);
				DBOperation.DELETE_ALL.getDbunitOperation().execute(configuration.getDatabaseConnection(), dataSet);
				initializers.get(dataSetName).initDatabase();
			}
			else {
				return null;
			}
			
			IDataSet dataSet = configuration.getDatabaseConnection().createDataSet();
			dataSet = DBUnitUtils.filter(dataSet, configuration);
			try {
				// Write to memory byte array
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				FlatXmlDataSet.write(dataSet, baos);
				
				// Rebuild DataSet from memory byte array and put in cache
				FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
				builder.setCaseSensitiveTableNames(true);
				builder.setColumnSensing(true);
				builder.setDtdMetadata(true);
				
				dataSet = builder.build(new ByteArrayInputStream(baos.toByteArray()));
				cachedDataSets.put(dataSetName, dataSet);
				logger.info("DataSet added in cache : '" + dataSetName + "'.");
			} catch (DataSetException e) {
				logger.error("unable to generate dataset", e);
			} catch (IOException e) {
				logger.error("unable to generate dataset", e);
			}
		}
		return cachedDataSets.get(dataSetName);
	}
	
	
}
