package com.jdbc_1.bean;

public class UserTable {
    private String user;
    private String password;
    private int balance;

    public UserTable() {
    }

    public UserTable(String user, String password, int balance) {
        this.user = user;
        this.password = password;
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "User [balance=" + balance + ", password=" + password + ", user=" + user + "]";
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
    
}
