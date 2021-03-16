package com.jdbc_1.statement.crud;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

import com.jdbc_1.bean.Customer;
import com.jdbc_1.bean.Orders;
import com.jdbc_1.util.JDBCUtils;

import org.junit.Test;

/**
 * 使用PreparedStatement实现针对不同表的通用的查询操作
 */
public class PreparedStatementQuery {
    
    /**
     * 返回单个对象
     */
    public<T> T getInstance(Class<T> clazz, String sql, Object... args) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();
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
            JDBCUtils.closeResource(conn, ps, rs);
        }
        return null;
    }

    /**
     * 返回多个对象
     */
    public<T> List<T> getForList(Class<T> clazz, String sql, Object... args) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();
            ps = conn.prepareStatement(sql);
            
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            rs = ps.executeQuery();
            // 获取结果集的元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            // 通过ResultSetMetaData获取结果集中的列数
            int columnCount = rsmd.getColumnCount();
            List<T> list = new ArrayList<>();
            while (rs.next()) {
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
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps, rs);
        }
        return null;
    }

    @Test
    public void testGetInstance() {
        String sql = "select id, name, email from customers where id = ?;";
        Customer customer = getInstance(Customer.class, sql, 10);
        System.out.println(customer);
    
        sql = "select order_id id, order_date date from orders where order_id = ?;";
        Orders orders = getInstance(Orders.class, sql, 2);
        System.out.println(orders);

        sql = "select order_id id, order_date date from orders where order_id < ?;";
        List<Orders> list = getForList(Orders.class, sql, 3);
        // list.forEach(s -> System.out.println(s));
        list.forEach(System.out::println);

        System.out.println();
        sql = "select id, name, email from customers where id < ?;";
        List<Customer> list2 = getForList(Customer.class, sql, 12);
        list2.forEach(s -> System.out.println(s));

    }
}
