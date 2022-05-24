package org.arkaan.simpleatm.datamodel;

public class Card {
    private String pin;
    private final Customer customer;
    private final String accountNumber;

    public Card(String pin, String username, double initialBalance, String accountNumber) {
        this.pin = pin;
        customer = new Customer(username, initialBalance);
        this.accountNumber = accountNumber;
    }

    public String getPin() {
        return pin;
    }

    Customer getCustomer() {
        return customer;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public double getAccountBalance() {
        return customer.getBalance();
    }

    public String getAccountNumber() {
        return accountNumber;
    }
}
