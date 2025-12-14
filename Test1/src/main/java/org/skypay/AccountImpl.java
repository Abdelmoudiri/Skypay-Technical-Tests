package org.skypay;

import javax.swing.text.html.parser.Parser;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AccountImpl implements Account {

    private final List<Transaction> transactions = new ArrayList<>();
    private int balance = 0;

    @Override
    public void deposit(int amount, String date) {
        if (amount <= 0)
            throw new IllegalArgumentException("Deposit amount must be positive");

        balance += amount;
        transactions.add(new Transaction(date, amount, balance));
    }

    @Override
    public void withdraw(int amount, String date) {
        if (amount <= 0)
            throw new IllegalArgumentException("Withdraw amount must be positive");

        if (amount > balance)
            throw new IllegalArgumentException("Insufficient funds");

        balance -= amount;
        transactions.add(new Transaction(date, -amount, balance));
    }

    

    @Override
    public void printStatement() {
        System.out.println("DATE | AMOUNT | BALANCE");

        List<Transaction> reversed = new ArrayList<>(transactions);
        Collections.reverse(reversed);

        for (Transaction t : reversed) {
            System.out.println(
                    t.getDate() + " | " + t.getAmount() + " | " + t.getBalance()
            );
        }
    }
    
        List<String> getTransactionDates(){
           return this.transactions.stream().map(t->t.getDate()).distinct().collect(Collectors.toList());
        }
}
