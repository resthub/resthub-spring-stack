package org.resthub.test.dbunit;

import javax.sql.DataSource;

import org.dbunit.IDatabaseTester;
import org.dbunit.dataset.ITable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.resthub.test.dbunit.annotation.DBOperation;
import org.resthub.test.dbunit.annotation.InjectDataSet;
import org.resthub.test.dbunit.utils.DelegateDataSourceDatabaseTester;
import org.resthub.test.dbunit.utils.MyApplicationDataSetInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;

@ContextConfiguration(locations = { "classpath:test-context.xml",
		"classpath:application-context.xml" })
@TransactionConfiguration(defaultRollback = false)
public abstract class AbstractTestToolsTestCase extends AbstractDBUnitTestCase {

	private IDatabaseTester databaseTester;

	@Autowired
	private DataSource dataSource;

	@Before
	public void init() {
		databaseTester = new DelegateDataSourceDatabaseTester(dataSource);
	}

	@Test
	@InjectDataSet("dataset")
	public void testSomeMethod() throws Exception {
		ITable table1 = databaseTester.getConnection().createTable("table1");
		Assert.assertEquals(1, table1.getRowCount());
	}

	@Test
	@InjectDataSet("datasetInitializer")
	public void testSomeOtherMethod() throws Exception {
		ITable table1 = databaseTester.getConnection().createTable("table1");
		Assert.assertEquals(MyApplicationDataSetInitializer.NB_ROWS,
				table1.getRowCount());
	}

	@Test
	@InjectDataSet("datasetInitializer")
	public void testSomeOtherMethodWithSameDataSet() throws Exception {
		ITable table1 = databaseTester.getConnection().createTable("table1");
		Assert.assertEquals(MyApplicationDataSetInitializer.NB_ROWS,
				table1.getRowCount());
	}

	@Test
	@InjectDataSet(value = { "datasetInitializer", "datasetInitializer" }, dbOperation = DBOperation.REFRESH)
	public void testWithSameDataSetTwice() throws Exception {
		ITable table1 = databaseTester.getConnection().createTable("table1");
		Assert.assertEquals(MyApplicationDataSetInitializer.NB_ROWS,
				table1.getRowCount());
	}

	@Test
	@InjectDataSet(value = { "datasetInitializer", "dataset" })
	public void testWithMixedDataSetTypes() throws Exception {
		ITable table1 = databaseTester.getConnection().createTable("table1");
		Assert.assertEquals(MyApplicationDataSetInitializer.NB_ROWS + 1,
				table1.getRowCount());
	}

}
