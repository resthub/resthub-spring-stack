package org.resthub.test.dbunit.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.dbunit.dataset.CompositeDataSet;
import org.dbunit.dataset.FilteredDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.filter.DefaultTableFilter;
import org.dbunit.dataset.filter.ExcludeTableFilter;
import org.dbunit.dataset.filter.ITableFilter;
import org.dbunit.dataset.filter.IncludeTableFilter;
import org.resthub.test.dbunit.annotation.CleanupDB;
import org.resthub.test.dbunit.annotation.DBOperation;
import org.resthub.test.dbunit.annotation.InjectDataSet;
import org.resthub.test.dbunit.config.DbUnitConfiguration;
import org.resthub.test.dbunit.resolver.CompositeDataSetResolver;
import org.resthub.test.dbunit.resolver.DataSetResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

/**
 * Extracts a DBUnit flat XML dataset from a database.
 * 
 * @author vanackej
 */
public class DBUnitUtils {

	private static final Logger logger = LoggerFactory.getLogger(DBUnitUtils.class);

	/**
	 * Resolver to use, not injected
	 */
	private DataSetResolver dataSetResolver;

	public DBUnitUtils(DataSetResolver resolver) {
		this(Arrays.asList(resolver));
	}

	public DBUnitUtils(DataSetResolver... resolvers) {
		this(Arrays.asList(resolvers));
	}

	public DBUnitUtils(List<DataSetResolver> resolvers) {
		Assert.notEmpty(resolvers, "At least one resolver must be provided");
		if (resolvers.size() == 1) {
			dataSetResolver = resolvers.get(0);
		} else {
			dataSetResolver = new CompositeDataSetResolver(resolvers);
		}

	}

	/**
	 * Performs a cleanup operation. If no tables filters are specified in
	 * configuration, data from entire database will be cleanup. Otherwise, a
	 * partial cleanup will be performed.
	 * 
	 * @throws Exception
	 */
	public void cleanup(DbUnitConfiguration configuration, CleanupDB cleanupDB) throws Exception {
		IDataSet dataSet = filter(configuration.getDatabaseConnection().createDataSet(), configuration);
		DBOperation.DELETE_ALL.getDbunitOperation().execute(configuration.getDatabaseConnection(), dataSet);
		// configuration.getDatabaseConnection().close();
		logger.info("Cleanup completed");
	}

	/**
	 * Performs a inject operation. If no tables filters are specified in
	 * configuration, the whole dataset is injected. Otherwise, only a subset
	 * will be injected.
	 * 
	 * @throws Exception
	 */
	public void inject(DbUnitConfiguration configuration, InjectDataSet injectDataSet) throws Exception {

		if (injectDataSet.value() == null || injectDataSet.value().length == 0) {
			// nothing to inject
			return;
		}

		IDataSet idataSet;
		if (injectDataSet.value().length == 1) {
			String datasetLocation = injectDataSet.value()[0];
			logger.info("Inject dataset '" + datasetLocation + "'");
			idataSet = createDataSet(datasetLocation, configuration);
		} else {
			List<IDataSet> datasets = new ArrayList<IDataSet>();
			logger.info("Inject datasets : " + Arrays.toString(injectDataSet.value()));
			for (String datasetLocation : injectDataSet.value()) {
				datasets.add(createDataSet(datasetLocation, configuration));
			}
			idataSet = new CompositeDataSet(datasets.toArray(new IDataSet[datasets.size()]));
		}
		idataSet = filter(idataSet, configuration);
		injectDataSet.dbOperation().getDbunitOperation().execute(configuration.getDatabaseConnection(), idataSet);
		logger.info("Injection completed");
	}

	private IDataSet createDataSet(String datasetName, DbUnitConfiguration configuration) throws Exception {
		IDataSet dataSet = dataSetResolver.resolveDataSet(datasetName);
		if (dataSet == null) {
			throw new IllegalArgumentException("Unable to resolve dataset named : " + datasetName);
		}
		return dataSet;
	}

	/**
	 * Filters the dataset according to includes and excludes in the configuration.
	 * 
	 * @param source
	 * @param configuration
	 * @return
	 */
	public static IDataSet filter(IDataSet source, DbUnitConfiguration configuration) {
		List<String> excludeTables = configuration.getExcludeTables();
		List<String> includeTables = configuration.getIncludeTables();
		IDataSet dataSetToUse;
		boolean hasExcludeTables = !CollectionUtils.isEmpty(excludeTables);
		boolean hasIncludeTables = !CollectionUtils.isEmpty(includeTables);
		if (hasExcludeTables || hasIncludeTables) {
			ITableFilter tableFilter;
			if (hasExcludeTables && hasIncludeTables) {
				DefaultTableFilter defaultTableFilter = new DefaultTableFilter();
				for (String table : includeTables) {
					defaultTableFilter.includeTable(table);
				}
				for (String table : excludeTables) {
					defaultTableFilter.excludeTable(table);
				}
				tableFilter = defaultTableFilter;
			} else if (hasExcludeTables) {
				tableFilter = new ExcludeTableFilter(excludeTables.toArray(new String[excludeTables.size()]));
			} else {
				tableFilter = new IncludeTableFilter(includeTables.toArray(new String[includeTables.size()]));
			}

			dataSetToUse = new FilteredDataSet(tableFilter, source);
		} else {
			dataSetToUse = source;
		}
		return dataSetToUse;
	}

}
