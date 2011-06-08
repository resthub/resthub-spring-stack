package org.resthub.test.dbunit;

import javax.sql.DataSource;

import org.dbunit.IDatabaseTester;
import org.dbunit.dataset.ITable;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.resthub.test.dbunit.annotation.DbUnitSpringJUnit4ClassRunner;
import org.resthub.test.dbunit.annotation.InjectDataSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;

@ContextConfiguration(locations = { "classpath:test-context.xml", "classpath:application-context.xml" , "classpath:hsql-test-context.xml"})
@TransactionConfiguration(defaultRollback = false)
@InjectDataSet(value="dataset", onceForClass=false)
@RunWith(DbUnitSpringJUnit4ClassRunner.class)
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
