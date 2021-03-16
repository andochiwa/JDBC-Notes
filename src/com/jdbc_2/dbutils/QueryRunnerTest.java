package com.jdbc_2.dbutils;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.jdbc_1.bean.Customer;
import com.jdbc_1.util.DRUIDUtils;
import com.jdbc_1.util.JDBCUtils;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.Test;

/**
 * commons-dbutils是Apache组织提供一个开源JDBC工具类库，封装了针对于数据库的crud操作
 */
public class QueryRunnerTest {

    @Test
    public void testInsert() {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = DRUIDUtils.getConnection();
            String sql = "insert into customers(name, email, birth) values(?, ?, ?)";
            int insertCount = runner.update(conn, sql, "缪斯", "mius@gmail.com", "1997-09-08");
            System.out.println("添加了" + insertCount + "条记录");
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    /**
     * BeanHandler: 是ResultSetHandler的一个实现类，用来封装表中一条记录 ArrayHandlier: 数组形式封装
     * MapHandler: map形式封装 ScalarHandler:用于特殊字段, count, sum等
     */
    @Test
    public void testQuery1() {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = DRUIDUtils.getConnection();
            String sql = "select id, name, email, birth from customers where id = ?";
            BeanHandler<Customer> handler = new BeanHandler<>(Customer.class);
            Customer customer = runner.query(conn, sql, handler, 21);
            System.out.println(customer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    /**
     * BeanListHandler: 是ResultSetHandler的一个实现类，用来封装表中多条记录
     */
    @Test
    public void testQuery2() {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = DRUIDUtils.getConnection();
            String sql = "select id, name, email, birth from customers where id < ?";
            BeanListHandler<Customer> handler = new BeanListHandler<>(Customer.class);
            List<Customer> list = runner.query(conn, sql, handler, 11);
            list.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    /**
     * MapListHandler: 是ResultSetHandler的一个实现类，用key-value保存字段封装多条记录
     */
    @Test
    public void testQuery3() {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = DRUIDUtils.getConnection();
            String sql = "select id, name, email, birth from customers where id < ?";
            MapListHandler handler = new MapListHandler();
            List<Map<String, Object>> list = runner.query(conn, sql, handler, 11);
            list.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    /**
     * ScalarHandler:用于特殊字段, count, sum等
     */
    @Test
    public void testQuery4() {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = DRUIDUtils.getConnection();
            String sql = "select max(birth) from customers";
            ScalarHandler<Object> handler = new ScalarHandler<>();
            Object list = runner.query(conn, sql, handler);
            System.out.println(list);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    /**
     * 自定义ResultSethandler的实现类
     */
    @Test
    public void testQuery5() {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = DRUIDUtils.getConnection();
            String sql = "select id, name, email, birth from customers where id = ?";
            ResultSetHandler<Customer> handler = new ResultSetHandler<Customer>() {

                @Override
                public Customer handle(ResultSet rs) throws SQLException {
                    if(rs.next()) {
                        int id = rs.getInt("id");
                        String name = rs.getString("name");
                        String email = rs.getString("email");
                        Date birth = rs.getDate("birth");
                        Customer cus = new Customer(id, name, email, birth);
                        return cus;
                    }
                    return null;
                }
                
            };
            Object customer = runner.query(conn, sql, handler, 21);
            System.out.println(customer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }
}
