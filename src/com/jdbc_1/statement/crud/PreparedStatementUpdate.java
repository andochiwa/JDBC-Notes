package com.jdbc_1.statement.crud;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Properties;

import com.jdbc_1.util.JDBCUtils;

import org.junit.Test;

/**
 * 使用PreparedStatement实现增删改
 */
public class PreparedStatementUpdate {

    /**
     * 通用的增删改操作
     */
    public void update(String sql, Object... args) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            // 1.创建连接
            conn = JDBCUtils.getConnection();
            // 2.预编译sql语句
            ps = conn.prepareStatement(sql);
            // 3.填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            // 4.执行sql
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 5.关闭资源
            JDBCUtils.closeResource(conn, ps);
        }
    }

    /**
     * 修改customers表中的一条记录
     */
    @Test
    public void testUpdate() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            // 1.获取数据库的连接
            conn = JDBCUtils.getConnection();

            // 2.预编译sql语句，返回PreparedStatement的实例
            String sql = "update customers set name = ? where id = ?";
            ps = conn.prepareStatement(sql);

            // 3.填充占位符
            ps.setObject(1, "莫扎特");
            ps.setObject(2, "18");

            // 4.执行sql
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 5.资源的关闭
            JDBCUtils.closeResource(conn, ps);
        }
    }

    /**
     * 删除customers中的记录
     */
    @Test
    public void testDelete() {
        // String sql = "delete from customers where id = ?";
        // update(sql, 19);

        String sql = "alter table order1 rename to orders";
        update(sql);
    }
    

    // 向customers中添加记录
    @Test
    public void testinsert() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            InputStream is = new FileInputStream("D:\\code\\java\\JDBC\\src\\jdbc.properties");

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

            // 4.预编译sql语句，返回PreparedStatment实例
            String sql = "insert into customers(name, email, birth)values(?, ?, ?)";
            ps = conn.prepareStatement(sql);

            // 5.填充占位符
            ps.setString(1, "哪吒");
            ps.setString(2, "nezha@gmail.com");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date parse = sdf.parse("1000-01-01");
            ps.setDate(3, new Date(parse.getTime()));

            // 6.执行sql
            ps.execute();
        } catch (ClassNotFoundException | IOException | SQLException | ParseException e) {
            e.printStackTrace();
        } finally {
            // 7.关闭资源
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
        

    }
}
