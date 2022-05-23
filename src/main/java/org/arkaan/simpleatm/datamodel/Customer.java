package org.arkaan.simpleatm.datamodel;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private final String name;
    private double balance;
    private List<Transaction> transactionList;

    public Customer(String name, double balance) {
        this.name = name;
        this.balance = balance;
    }

    public void addBalance(double amount) {
        balance += amount;
    }

    public void reduceBalance(double amount) {
        balance -= amount;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public void addTransaction(Transaction transaction) {
        if (transactionList == null) transactionList = new ArrayList<>();
        transactionList.add(transaction);
    }

    public List<Transaction> getTransactionList() {
        return transactionList;
    }
}
