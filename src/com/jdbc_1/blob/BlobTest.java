package com.jdbc_1.blob;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.jdbc_1.bean.Customer;
import com.jdbc_1.util.JDBCUtils;

import org.junit.Test;

/**
 * 测试使用PreparedStatement操作Blob类型数据
 */
public class BlobTest {
    /**
     * 向数据表customers中插入Blob类型的字段
     * 
     */
    @Test
    public void testInsert() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "insert into customers(name, email, birth, photo) values(?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setObject(1, "张宇豪");
            ps.setObject(2, "zhang@qq.com");
            ps.setObject(3, "1992-09-08");
            ps.setBlob(4, new FileInputStream("playgirl.jpg"));

            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps);
        }
    }

    /**
     * 查询数据表customers中blob字段
     */
    @Test
    public void testQuery() {
        Connection conn = null;
        PreparedStatement ps = null;
        InputStream is = null;
        FileOutputStream fos = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "select id, name, email, birth, photo from customers where id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, 20);
            is = null;
            fos = null;

            rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                Date birth = rs.getDate("birth");

                Customer customer = new Customer(id, name, email, birth);
                System.out.println(customer);

                // photo需要以流的方式获取
                // 讲blob字段以文件的方式下载下来保存在本地
                Blob photo = rs.getBlob("photo");
                is = photo.getBinaryStream();
                fos = new FileOutputStream("zhangyuhao.jpg");
                byte[] buffer = new byte[1024];
                for (int len = is.read(buffer); len != -1; len = is.read(buffer)) {
                    fos.write(buffer, 0, len);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            JDBCUtils.closeResource(conn, ps, rs);
        }
    }
}
