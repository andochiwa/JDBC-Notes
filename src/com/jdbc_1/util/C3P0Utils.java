package com.jdbc_1.util;

import java.sql.Connection;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class C3P0Utils {

    private static ComboPooledDataSource cpds = new ComboPooledDataSource("helloC3P0");
    public static Connection getConnection() throws SQLException {
        Connection conn = cpds.getConnection();
        return conn;
    }
}
