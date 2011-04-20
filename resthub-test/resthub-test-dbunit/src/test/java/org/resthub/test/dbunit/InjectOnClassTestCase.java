package org.resthub.test.dbunit;

import javax.sql.DataSource;

import org.dbunit.IDatabaseTester;
import org.dbunit.dataset.ITable;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.resthub.test.dbunit.annotation.InjectDataSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

@ContextConfiguration(locations = { "classpath:test-context.xml", "classpath:application-context.xml" , "classpath:hsql-test-context.xml"})
@TransactionConfiguration(defaultRollback = false)
@InjectDataSet(value="dataset", onceForClass=false)
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class,
		DbUnitTestExecutionListener.class,
		TransactionalTestExecutionListener.class })
public class InjectOnClassTestCase {

	@Autowired
	private IDatabaseTester databaseTester;
	
	@Autowired
	private DataSource dataSource;
	
	@Test
	public void shouldHaveInjectedDataSet() throws Exception {
		ITable table1 = databaseTester.getConnection().createTable("table2");
		Assert.assertEquals(1, table1.getRowCount());
	}
	
	@Test
	public void insertDataInTable2() throws Exception {
		JdbcTemplate template = new JdbcTemplate(dataSource);
		template.execute("insert into table2 values (4)");
		ITable table1 = databaseTester.getConnection().createTable("table2");
		Assert.assertEquals(2, table1.getRowCount());
	}
	
	@Test
	public void shouldHaveResestDataSet() throws Exception {
		ITable table1 = databaseTester.getConnection().createTable("table2");
		Assert.assertEquals(1, table1.getRowCount());
	}
}
