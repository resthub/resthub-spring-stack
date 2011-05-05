package org.resthub.test.dbunit.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.sql.DataSource;

import org.resthub.test.dbunit.initializer.DatabaseInitializer;
import org.springframework.beans.factory.annotation.Autowired;

public class MyApplicationDataSetInitializer implements DatabaseInitializer {

	public static final int NB_ROWS = 10;

	public static final int START_INDEX = 10;

	@Autowired
	private DataSource dataSource;

	@Override
	public void initDatabase() throws Exception {
		Connection connection = dataSource.getConnection();
		PreparedStatement table1Statement = connection.prepareStatement("insert into table2 values (?)");
		PreparedStatement table2Statement = connection.prepareStatement("insert into table1 values (?,?)");
		try {
			for (int i = 0; i < NB_ROWS; i++) {
				int actualIndex = i + START_INDEX;
				table1Statement.setInt(1, actualIndex);
				table1Statement.execute();
				table2Statement.setInt(1, actualIndex);
				table2Statement.setInt(2, actualIndex);
				table2Statement.execute();
			}
		} finally {
			table1Statement.close();
			table2Statement.close();
			connection.close();
		}
	}

}
