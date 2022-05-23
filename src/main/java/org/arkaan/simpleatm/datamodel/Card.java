package org.arkaan.simpleatm.datamodel;

public class Card {
    private String pin;
    private final Customer customer;

    public Card(String pin, String username, double initialBalance) {
        this.pin = pin;
        customer = new Customer(username, initialBalance);
    }

    public String getPin() {
        return pin;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
