package org.arkaan.simpleatm.datamodel;

import java.util.Objects;

public class Account {
    
    private final int accountNumber;
    private final int pin;
    private final String name;
    private int balance;

    public Account(int accountNumber, int pin, String name, int balance) {
        this.name = name;
        this.balance = balance;
        this.pin = pin;
        this.accountNumber = accountNumber;
    }

    public void addBalance(int amount) {
        balance += amount;
    }

    public void reduceBalance(int amount) {
        balance -= amount;
    }

    public String getName() {
        return name;
    }

    public int getBalance() {
        return balance;
    }
    
    public int getAccountNumber() {
        return accountNumber;
    }
    
    public int getPin() {
        return pin;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(accountNumber);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Account)) {
            return false;
        }
        Account other = (Account) obj;
        return Objects.equals(accountNumber, other.accountNumber);
    }
    
    @Override
    public String toString() {
        return String.join(",", name, String.valueOf(pin), String.valueOf(balance), String.valueOf(accountNumber), "\n");
    }
}
