package com.jdbc_1.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * 操作数据库的工具类
 */
public class JDBCUtils {

    /**
     * 获取数据库的连接
     */
    public static Connection getConnection() throws Exception{
        Connection conn = null;
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");

        // 1.读取配置文件中的信息
        Properties pros = new Properties();
        pros.load(is);
        String url = pros.getProperty("url");
        String user = pros.getProperty("user");
        String password = pros.getProperty("password");

        // 2.加载驱动
        Class.forName(pros.getProperty("driverClass"));

        // 3.获取连接
        conn = DriverManager.getConnection(url, user, password);

        return conn;
    }

    /**
     * 关闭连接和资源的操作
     */
    public static void closeResource(Connection conn, Statement ps) {
        try {
            if(conn != null)
                conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if(ps != null)
                ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭连接, 资源和结果集的操作
     */
    public static void closeResource(Connection conn, Statement ps, ResultSet rs) {
        try {
            if(conn != null)
                conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if(ps != null)
                ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if(rs != null)
                rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
