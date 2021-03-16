package com.jdbc_2.connection;

import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.junit.Test;

public class DBCPTest {

    /**
     * 测试DBCP的数据库连接技术
     * 方式一
     */
    @Test
    public void testGetConnection() throws Exception{
        // 创建了DBCP的数据库连接池
        BasicDataSource source = new BasicDataSource();

        // 设置基本信息
        source.setDriverClassName("com.mysql.cj.jdbc.Driver");
        source.setUrl("jdbc:mysql://localhost:3306/test?serverTimezone=GMT%2B9:00&rewriteBatchedStatements=true");
        source.setUsername("root");
        source.setPassword("root");
        // 设置数据库连接池管理的相关属性
        source.setInitialSize(10);
        source.setMaxTotal(10);

        Connection conn = source.getConnection();
        System.out.println(conn);
        source.close();
    }

    /**
     * 方式二
     */
    @Test
    public void testGetConnection2() throws Exception{
        Properties pros = new Properties();
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");
        pros.load(is);
        BasicDataSource source = BasicDataSourceFactory.createDataSource(pros);
        Connection conn = source.getConnection();
        System.out.println(conn);
    }
}
