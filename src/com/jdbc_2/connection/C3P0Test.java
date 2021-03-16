package com.jdbc_2.connection;

import java.sql.Connection;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import org.junit.Test;

public class C3P0Test {
    /**
     * 方式一
     */
    @Test
    public void testGetConnection() throws Exception {
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        cpds.setDriverClass( "com.mysql.cj.jdbc.Driver" ); //loads the jdbc driver            
        cpds.setJdbcUrl( "jdbc:mysql://localhost:3306/test?serverTimezone=GMT%2B9:00&rewriteBatchedStatements=true" );
        cpds.setUser("root");                                  
        cpds.setPassword("root");
        // 通过设置相关的参数，对数据库连接池进行管理
        // 设置初始时数据库连接池中的连接数
        cpds.setInitialPoolSize(10);

        Connection conn = cpds.getConnection();
        System.out.println(conn);
        
    }

    /**
     * 方式二：使用配置文件
     */
    @Test
    public void testGetConnection1() throws Exception{
        ComboPooledDataSource cpds = new ComboPooledDataSource("helloC3P0");
        Connection conn = cpds.getConnection();
        System.out.println(conn);
    }
}
