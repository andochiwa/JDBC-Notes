package com.jdbc_2.DAO2;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.jdbc_1.util.JDBCUtils;

/**
 * 封装了针对于数据表的通用的操作
 */
@SuppressWarnings("unchecked")
public abstract class DAO<T> {
    private Class<T> clazz;
    
    {
        Type generic = this.getClass().getGenericSuperclass();
        ParameterizedType paramType = (ParameterizedType) generic;
        
        Type[] typeArguments = paramType.getActualTypeArguments(); // 获取了父类的泛型参数
        clazz = (Class<T>) typeArguments[0];
    }

    /**
     * 通用的增删改操作
     * 
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
     * 
     * @version 2.0
     */
    public T getInstance(Connection conn, String sql, Object... args) {
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
     * 通用的查询操作，返回多条记录（针对事务）
     * 
     * @version 2.0
     */
    public List<T> getForList(Connection conn, String sql, Object... args) {
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
            JDBCUtils.closeResource(null, ps, rs);
        }
        return null;
    }

    public <E> E getValue(Connection conn, String sql, Object... args) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            rs = ps.executeQuery();
            if (rs.next()) {
                return (E) rs.getObject(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null, ps, rs);
        }
        return null;
    }
}