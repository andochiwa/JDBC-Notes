package com.jdbc_1.statement.crud;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import com.jdbc_1.bean.Customer;
import com.jdbc_1.util.JDBCUtils;

import org.junit.Test;

public class CustomerQuery {

    /**
     * 对于customers表通用的查询操作
     */
    public Customer customersQuery(String sql, Object... args) {
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
                Customer customer = new Customer();
                for (int i = 0; i < columnCount; i++) {
                    // 获取列值
                    Object value = rs.getObject(i + 1);

                    // 获取每个列的列名 不推荐使用
                    // String columnName = rsmd.getColumnName(i + 1);
                    // 获取列的别名
                    String columnLabel = rsmd.getColumnLabel(i + 1);


                    // 给customer对象指定的某个属性赋值为value, 通过反射
                    Field field = Customer.class.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(customer, value);
                }
                return customer;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps, rs);
        }
        return null;
    }

    @Test
    public void test2Query2() {
        String sql = "select id, email, birth from customers where id = ?;";
        Customer customer = customersQuery(sql, 10);
        System.out.println(customer);
    }

    @Test
    public void testQuery1() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "select id, name, email, birth from customers where id = ?;";
            ps = conn.prepareStatement(sql);
            ps.setObject(1, 5);
            
            // 执行并返回结果集
            resultSet = ps.executeQuery();
            // 处理结果集
            if (resultSet.next()) { // 如果有数据返回true并指针下移，如果false指针不会下移
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String email = resultSet.getString(3);
                Date birth = resultSet.getDate(4);

                Customer customer = new Customer(id, name, email, birth);
                System.out.println(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            JDBCUtils.closeResource(conn, ps, resultSet);
        }

    }
}
