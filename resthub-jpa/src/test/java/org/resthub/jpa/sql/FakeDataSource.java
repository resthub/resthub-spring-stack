package org.resthub.jpa.sql;

import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.logging.Logger;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FakeDataSource extends JdbcDataSource implements DataSource {

    private Connection mockedConnection = mock(Connection.class);

    public FakeDataSource() {
        try {
            when(this.mockedConnection.createStatement()).thenReturn(mock(Statement.class));
            when(this.mockedConnection.getMetaData()).thenReturn(mock(DatabaseMetaData.class));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return this.mockedConnection;
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return this.mockedConnection;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}
