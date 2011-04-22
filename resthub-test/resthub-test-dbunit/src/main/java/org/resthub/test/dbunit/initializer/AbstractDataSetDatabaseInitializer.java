package org.resthub.test.dbunit.initializer;

import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.resthub.test.dbunit.annotation.DBOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * Base class for DatabaseInitializer based on DBUnit {@link IDataSet}
 * 
 * @author vanackej
 *
 */
public abstract class AbstractDataSetDatabaseInitializer implements DatabaseInitializer, ResourceLoaderAware, InitializingBean {

	private Logger logger = LoggerFactory.getLogger(AbstractDataSetDatabaseInitializer.class);

	private ResourceLoader resourceLoader;
	
	private String location;

	private IDataSet dataSet;
	
	@Autowired
	private IDatabaseConnection connection;

	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	/**
	 * Set the location if the the resource.
	 * 
	 * @param pattern
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	public IDatabaseConnection getConnection() {
		return connection;
	}

	public void setConnection(IDatabaseConnection connection) {
		this.connection = connection;
	}

	@Override
	public void initDatabase() throws Exception {
		DBOperation.INSERT.getDbunitOperation().execute(connection, dataSet);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Resource resource = resourceLoader.getResource(location);
		if (!resource.exists()) {
			throw new BeanCreationException("No resource found at specified location : " + location);
		}
		logger.debug("Loading dataSet from resource : " + resource.toString());
		this.dataSet = loadDataSet(resource);
	}

	protected abstract IDataSet loadDataSet(Resource resource) throws Exception;
	
}
