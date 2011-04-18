package org.resthub.test.dbunit.resolver;

import java.io.InputStream;
import java.util.List;

import org.dbunit.dataset.IDataSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * A {@link DataSetResolver} that delegates resolution to child resolvers 
 * 
 * @author vanackej
 *
 */
public class CompositeDataSetResolver implements DataSetResolver {

	private Logger logger = LoggerFactory.getLogger(CompositeDataSetResolver.class);
	
	private List<DataSetResolver> resolvers;

	public CompositeDataSetResolver(List<DataSetResolver> resolvers) {
		Assert.notEmpty(resolvers,"At least one resolver must be provided");
		this.resolvers = resolvers;
	}
	
	@Override
	public IDataSet resolveDataSet(String dataSetName) throws Exception {
		for (DataSetResolver resolver : resolvers) {
			IDataSet dataSet = resolver.resolveDataSet(dataSetName);
			if (dataSet != null) {
				return dataSet;
			}
		}
		return null;
	}

}
