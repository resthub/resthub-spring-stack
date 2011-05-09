package org.resthub.test.dbunit.initializer;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.springframework.core.io.Resource;

/**
 * DatabaseInitializer implementation based on DBUnit {@link FlatXmlDataSet}
 * 
 * @author vanackej
 * 
 */
public class FlatXMLDatabaseInitializer extends AbstractDataSetDatabaseInitializer  {

	@Override
	protected IDataSet loadDataSet(Resource resource) throws Exception  {
		FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
		builder.setCaseSensitiveTableNames(true);
		builder.setColumnSensing(true);
		builder.setDtdMetadata(true);
		return builder.build(resource.getInputStream());
	}
	
}
