package com.jdbc_2.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

import com.jdbc_1.bean.Customer;

public class CustomerDAOImpl extends DAO implements CustomerDAO {

    @Override
    public void insert(Connection conn, Customer customer) {
        String sql = "insert into customers(name, email, birth)values(?, ?, ?)";
        update(conn, sql, customer.getName(), customer.getEmail(), customer.getBirth());

    }

    @Override
    public void deleteByid(Connection conn, int id) {
        String sql = "delete from customers where id = ?";
        update(conn, sql, id);

    }

    @Override
    public void update(Connection conn, Customer customer) {
        String sql = "update customers set name = ?, email = ?, birth = ? where id = ?";
        update(conn, sql, customer.getName(), customer.getEmail(), customer.getBirth(), customer.getId());
    }

    @Override
    public Customer getCustomerByid(Connection conn, int id) {
        String sql = "select id, name, email, birth from customers where id = ?";
        Customer customer = getInstance(conn, Customer.class, sql, id);
        return customer;
    }

    @Override
    public List<Customer> getAll(Connection conn) {
        String sql = "select id, name, email, birth from customers";
        List<Customer> list = getForList(conn, Customer.class, sql);
        return list;
    }

    @Override
    public long getCount(Connection conn) {
        String sql = "select count(*) from customers";
        return getValue(conn, sql);
    }

    @Override
    public Date getMaxBirth(Connection conn) {
        String sql = "select max(birth) from customers";
        return getValue(conn, sql);
    }
    
}
