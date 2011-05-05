package org.resthub.test.dbunit.initializer;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.XmlDataSet;
import org.springframework.core.io.Resource;

/**
 * DatabaseInitializer implementation based on DBUnit {@link XmlDataSet}
 * 
 * @author vanackej
 * 
 */
public class XMLDatabaseInitializer extends AbstractDataSetDatabaseInitializer  {

	@Override
	protected IDataSet loadDataSet(Resource resource) throws Exception  {
		return new XmlDataSet(resource.getInputStream());
	}
	
}
