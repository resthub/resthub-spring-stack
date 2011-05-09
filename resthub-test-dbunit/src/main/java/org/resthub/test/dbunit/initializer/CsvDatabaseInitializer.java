package org.resthub.test.dbunit.initializer;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.csv.CsvDataSet;
import org.springframework.core.io.Resource;

/**
 * DatabaseInitializer implementation based on DBUnit {@link CsvDataSet}
 * 
 * @author vanackej
 * 
 */
public class CsvDatabaseInitializer extends AbstractDataSetDatabaseInitializer  {

	@Override
	protected IDataSet loadDataSet(Resource resource) throws Exception  {
		return new CsvDataSet(resource.getFile());
	}
	
}
