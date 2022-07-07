package org.arkaan.simpleatm.dto;

public class TransferDto {
    private final int destination;
    private final int amount;
    private final String ref;
    private final int balance;
    private final String date;

    public TransferDto(int destination, int amount, String ref, int balance, String date) {
        this.destination = destination;
        this.amount = amount;
        this.ref = ref;
        this.balance = balance;
        this.date = date;
    }

    public int getDestination() {
        return destination;
    }

    public int getAmount() {
        return amount;
    }

    public String getRef() {
        return ref;
    }

    public int getBalance() {
        return balance;
    }

    public String getDate() {
        return date;
    }
}
