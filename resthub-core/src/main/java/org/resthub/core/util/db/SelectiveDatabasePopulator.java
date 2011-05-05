package org.resthub.core.util.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.util.Assert;

/**
 * Delegates to the appropriate {@link DatabasePopulator} depending on the
 * database product name. (e.g. "hsql", "h2", "mysql", ...)
 * 
 * @author vanackej
 * 
 */
public class SelectiveDatabasePopulator implements DatabasePopulator {

    private Map<String, DatabasePopulator> exceptions = new HashMap<String, DatabasePopulator>();

    private DatabasePopulator databasePopulator;

    /**
     * The {@link DatabasePopulator} to use to populate the data source.
     * Mandatory with no default.
     * 
     * @param databasePopulator
     *            the database populator to use.
     */
    public void setDatabasePopulator(final DatabasePopulator databasePopulator) {
        this.databasePopulator = databasePopulator;
    }

    public void setExceptions(final Map<String, DatabasePopulator> exceptions) {
        this.exceptions = exceptions;
    }

    public Map<String, DatabasePopulator> getExceptions() {
        return exceptions;
    }

    @Override
    public void populate(final Connection connection) throws SQLException {
        Assert.state(this.databasePopulator != null,
                "DatabasePopulator must be provided");
        DatabasePopulator actualDatabasePopulator = this.databasePopulator;
        if (exceptions != null) {
            final DatabaseDescriptor descriptor = new DatabaseDescriptor(
                    connection);
            if (exceptions.containsKey(descriptor.getProductName())) {
                actualDatabasePopulator = exceptions.get(descriptor
                        .getProductName());
            }
        }

        actualDatabasePopulator.populate(connection);
    }

}
