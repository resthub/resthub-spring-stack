package org.resthub.test.dbunit.initializer;

import java.io.IOException;
import java.io.InputStream;

import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.dataset.xml.FlatXmlWriter;
import org.resthub.test.dbunit.annotation.DBOperation;
import org.resthub.test.dbunit.annotation.InjectDataSet;
import org.resthub.test.dbunit.config.DbUnitConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public class XMLDatabaseInitializer implements DatabaseInitializer, ResourceLoaderAware, InitializingBean {

	private Logger logger = LoggerFactory.getLogger(XMLDatabaseInitializer.class);

	private ResourceLoader resourceLoader;
	
	private String location;

	private FlatXmlDataSet dataSet;
	
	@Autowired
	private DbUnitConfiguration configuration;

	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	/**
	 * Set the location to resolve the resource using dataset name.
	 * 
	 * @param pattern
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public void initDatabase() throws Exception {
		DBOperation.INSERT.getDbunitOperation().execute(configuration.getDatabaseConnection(), dataSet);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Resource resource = resourceLoader.getResource(location);
		if (!resource.exists()) {
			throw new BeanCreationException("No resource found at specified location : " + location);
		}
		FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
		builder.setCaseSensitiveTableNames(true);
		builder.setColumnSensing(true);
		builder.setDtdMetadata(true);
		this.dataSet = builder.build(resource.getInputStream());
	}
	
}
