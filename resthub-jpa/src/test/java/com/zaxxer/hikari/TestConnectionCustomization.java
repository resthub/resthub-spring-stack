package com.zaxxer.hikari;

import java.sql.Connection;
import java.sql.SQLException;

public class TestConnectionCustomization implements IConnectionCustomizer {
    @Override
    public void customize(Connection connection) throws SQLException {

    }
}
