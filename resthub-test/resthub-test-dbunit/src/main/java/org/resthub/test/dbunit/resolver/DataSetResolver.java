package org.resthub.test.dbunit.resolver;

import org.dbunit.dataset.IDataSet;

/**
 * @author vanackej
 *
 */
public interface DataSetResolver {

	/**
	 * @param dataSetName
	 * @param configuration 
	 * @return the inputStream if found, or null
	 */
	IDataSet resolveDataSet(String dataSetName) throws Exception;
	
}
