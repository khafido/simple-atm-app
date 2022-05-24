package org.arkaan.simpleatm.datamodel;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private final String name;
    private double balance;
    private final List<Transaction> transactionList;

    Customer(String name, double balance) {
        transactionList = new ArrayList<>();
        this.name = name;
        this.balance = balance;
    }

    void addBalance(double amount) {
        balance += amount;
    }

    void reduceBalance(double amount) {
        balance -= amount;
    }

    String getName() {
        return name;
    }

    double getBalance() {
        return balance;
    }

    void addTransaction(Transaction transaction) {
        transactionList.add(transaction);
    }

    List<Transaction> getTransactionList() {
        return transactionList;
    }
}
