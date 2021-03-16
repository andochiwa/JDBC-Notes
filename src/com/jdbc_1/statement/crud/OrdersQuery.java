package com.jdbc_1.statement.crud;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import com.jdbc_1.bean.Orders;
import com.jdbc_1.util.JDBCUtils;

import org.junit.Test;

/**
 * 针对orders表的通用查询
 */
public class OrdersQuery {

    /**
     * 通用的orders表查询
     * 当orders对象属性名和表的列名不一致时，需要给表的列名起别名且别名要和对象属性名一致
     * 并且用getColumnLabel()来获取列的别名
     */
    public Orders ordersQuery(String sql, Object... args) {
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
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            if (rs.next()) {
                Orders orders = new Orders();
                for (int i = 0; i < columnCount; i++) {
                    Object value = rs.getObject(i + 1);
                    // 获取列的列名 不推荐使用
                    // String columnName = rsmd.getColumnName(i + 1);
                    // 获取列的别名
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    Field field = Orders.class.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(orders, value);
                }
                return orders;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps, rs);
        }
        return null;
    }

    @Test
    public void test1() {
        String sql = "select order_name as name from orders where order_id = ?;";
        Orders orders = ordersQuery(sql, 4);
        System.out.println(orders);
    }
}
