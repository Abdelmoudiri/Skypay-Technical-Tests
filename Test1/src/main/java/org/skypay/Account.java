package org.skypay;


public interface Account {
    void deposit(int amount, String date);
    void withdraw(int amount, String date);
    void printStatement();
}
