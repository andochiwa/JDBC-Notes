package com.jdbc_1.batch;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.jdbc_1.util.JDBCUtils;

import org.junit.Test;

/**
 * 使用PreparedStatement实现批量数据操作
 * 
 * update, delete本身具有批量操作
 * 
 * 题目: 向goods表中插入20000条数据 
 * create table goods ( 
    id int primary key auto_increment,
    name varchar(25) 
   );
 * 
 * 方式一: 使用Statement Connection conn = JDBCUtils.getConnection(); Statement st =
 * conn.createStatement(); for(int i = 0; i < 20000; i++) { String sql = "insert
 * into goods(name) values('name_" + i + ""')"; st.execute(sql); }
 * 
 */
public class insertTest {

    /**
     * 批量插入的方式二 使用PreparedStatement
     */
    @Test
    public void testInsert() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            long start = System.currentTimeMillis();
            conn = JDBCUtils.getConnection();
            String sql = "insert into goods(name)values(?)";
            ps = conn.prepareStatement(sql);
            for (int i = 1; i <= 200; i++) {
                ps.setObject(1, "name_" + i);

                ps.execute();
            }
            long end = System.currentTimeMillis();
            System.out.println(end - start);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps);
        }
    }

    /**
     * 批量插入的方法三
     * addBatch()、executeBatch()、clearBatch()
     * mysql服务器默认是关闭批处理的，我们需要通过一个参数让mysql开启批处理的支持
     * ?rewriteBatchStatements=true 写在配置文件的url后面
     */
    @Test
    public void test2Insert() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            long start = System.currentTimeMillis();
            conn = JDBCUtils.getConnection();
            String sql = "insert into goods(name)values(?)";
            ps = conn.prepareStatement(sql);
            for (int i = 1; i <= 10000; i++) {
                ps.setObject(1, "name_" + i);

                // "攒"sql
                ps.addBatch();
                if(i % 500 == 0) {
                    // 执行batch
                    ps.executeBatch();
                    // 清空batch
                    ps.clearBatch();
                }
            }
            long end = System.currentTimeMillis();
            System.out.println(end - start); // 6459
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps);
        }
    }

    /**
     * 方式四：设置连接不允许自动提交
     */
    @Test
    public void test3Insert() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            long start = System.currentTimeMillis();
            conn = JDBCUtils.getConnection();

            conn.setAutoCommit(false);

            String sql = "insert into goods(name)values(?)";
            ps = conn.prepareStatement(sql);
            for (int i = 1; i <= 10000; i++) {
                ps.setObject(1, "name_" + i);

                // "攒"sql
                ps.addBatch();
                if(i % 500 == 0) {
                    // 执行batch
                    ps.executeBatch();
                    // 清空batch
                    ps.clearBatch();
                }
            }
            conn.commit();
            long end = System.currentTimeMillis();
            System.out.println(end - start); // 4265
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps);
        }
    }
}
