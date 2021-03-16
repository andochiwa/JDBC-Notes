package com.jdbc_2.transaction;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.jdbc_1.bean.UserTable;
import com.jdbc_1.util.JDBCUtils;

import org.junit.Test;

public class TransactionTest {

    @Test
    public void testTransactionSelect() throws Exception {
        Connection conn = JDBCUtils.getConnection();
        // 设置隔离级别
        conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
        conn.setAutoCommit(false);
        String sql = "select user, password, balance from user_table where user = ?";
        UserTable user = getInstance(conn, UserTable.class, sql, "CC");
        System.out.println(user);
    }

    @Test
    public void testTransactionUpdate() throws Exception {
        Connection conn = JDBCUtils.getConnection();
        conn.setAutoCommit(false);
        String sql = "update user_table set balance = ? where user = ?";
        update(conn, sql, 5000, "CC");
        Thread.sleep(15000);
        System.out.println("修改结束");
    }

    @Test
    public void testUpdateWithTransaction() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            conn.setAutoCommit(false);
            String sql1 = "update user_table set balance = balance - 100 where user = ?";
            update(conn, sql1, "AA");

            // 模拟网络异常
            // System.out.println(10 / 0);

            String sql2 = "update user_table set balance = balance + 100 where user = ?";
            update(conn, sql2, "BB");
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            JDBCUtils.closeResource(conn, null);
        }
    }

    /**
     * 针对于user_table表来说
     * AA用户给BB用户转账100
     * update user_table set balance = balance - 100 where user = 'AA'
     * update user_table set balance = balance + 100 where user = 'BB'
     * 
     */
    @Test
    public void testupdate() {
        String sql1 = "update user_table set balance = balance - 100 where user = ?";
        update(sql1, "AA");

        // 模拟网络异常
        System.out.println(10 / 0);

        String sql2 = "update user_table set balance = balance + 100 where user = ?";
        update(sql2, "BB");

        
    }

    /**
     * 通用的增删改操作
     * @version 2.0
     */
    public void update(Connection conn, String sql, Object... args) {
        PreparedStatement ps = null;
        try {
            // 1.预编译sql语句
            ps = conn.prepareStatement(sql);
            // 2.填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            // 3.执行sql
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 4.关闭资源
            JDBCUtils.closeResource(null, ps);
        }
    }

    /**
     * 通用的查询操作，返回一条记录（针对事务）
     * @version 2.0
     */
    public<T> T getInstance(Connection conn, Class<T> clazz, String sql, Object... args) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            rs = ps.executeQuery();
            // 获取结果集的元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            // 通过ResultSetMetaData获取结果集中的列数
            int columnCount = rsmd.getColumnCount();
            if (rs.next()) {
                T t = clazz.getDeclaredConstructor().newInstance();
                for (int i = 0; i < columnCount; i++) {
                    // 获取列值
                    Object value = rs.getObject(i + 1);

                    // 获取每个列的列名 不推荐使用
                    // String columnName = rsmd.getColumnName(i + 1);
                    // 获取列的别名
                    String columnLabel = rsmd.getColumnLabel(i + 1);
                    

                    // 给t对象指定的某个属性赋值为value, 通过反射
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, value);
                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null, ps, rs);
        }
        return null;
    }

    /**
     * 通用的增删改操作
     * @version 1.0
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
}
