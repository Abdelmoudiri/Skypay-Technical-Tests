package org.skypay.entity;

public class User {
    private int id;
    private int balance;

    public User(int id, int balance) {
        this.id = id;
        this.balance = balance;
    }

    public int getId() { return id; }
    public int getBalance() { return balance; }
    public void setBalance(int balance) { this.balance = balance; }
    
    @Override
    public String toString() {
        return "User ID: " + id + ", Balance: " + balance;
    }
}