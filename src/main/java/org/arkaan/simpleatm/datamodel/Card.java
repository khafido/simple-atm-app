package org.arkaan.simpleatm.datamodel;

import java.util.Objects;

public class Card {
    private Integer pin;
    private final Account customer;
    private final Integer accountNumber;

    public Card(Integer pin, String username, Integer initialBalance, Integer accountNumber) {
        this.pin = pin;
        customer = new Account(username, initialBalance);
        this.accountNumber = accountNumber;
    }

    public Integer getPin() {
        return pin;
    }

    Account getCustomer() {
        return customer;
    }

    public void setPin(Integer pin) {
        this.pin = pin;
    }

    public Integer getAccountBalance() {
        return customer.getBalance();
    }

    public Integer getAccountNumber() {
        return accountNumber;
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
        if (!(obj instanceof Card)) {
            return false;
        }
        Card other = (Card) obj;
        return Objects.equals(accountNumber, other.accountNumber);
    }
}
