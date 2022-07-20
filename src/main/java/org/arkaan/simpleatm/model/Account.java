package org.arkaan.simpleatm.model;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private final int accountNumber;

    @Column(nullable = false)
    private final int pin;

    @Column(nullable = false)
    private final String name;

    @Column(nullable = false)
    private int balance;

    @OneToMany(mappedBy = "account")
    private List<Transaction> transactions;

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

    public int getId() {
        return id;
    }

    public List<Transaction> getTransactions() {
        return transactions;
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
