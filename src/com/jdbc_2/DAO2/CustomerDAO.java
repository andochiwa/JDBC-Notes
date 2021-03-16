package com.jdbc_2.DAO2;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

import com.jdbc_1.bean.Customer;

/**
 * 此接口用于规范对于customers表的常用操作
 */
public interface CustomerDAO {
    
    /**
     * 讲customer对象添加到数据库中
     */
    void insert(Connection conn, Customer customer);

    /**
     * 根据指定id删除表中记录
     */
    void deleteByid(Connection conn, int id);

    /**
     * 根据指定customer对象修改表中记录
     */
    void update(Connection conn, Customer customer);

    /**
     * 根据指定id查询对应Customer对象
     */
    Customer getCustomerByid(Connection conn, int id);

    /**
     * 查询表中所有记录的集合
     */
    List<Customer> getAll(Connection conn);

    /**
     * 返回数据表中数据的条目数
     */
    long getCount(Connection conn);

    /**
     * 返回数据表中最大的生日
     */
    Date getMaxBirth(Connection conn);
}
