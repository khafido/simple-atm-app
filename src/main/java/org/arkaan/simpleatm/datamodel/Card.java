package org.arkaan.simpleatm.datamodel;

import java.util.UUID;

public class Card {
    private final UUID uuid;
    private String pin;
    private final Customer customer;

    public Card(String pin, String username, double initialBalance) {
        this.pin = pin;
        uuid = UUID.fromString(username);
        customer = new Customer(username, initialBalance);
    }

    public UUID getUuid() {
        return uuid;
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
