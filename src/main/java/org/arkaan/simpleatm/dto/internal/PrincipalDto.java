package org.arkaan.simpleatm.dto.internal;

public class PrincipalDto {

    private final int accountNumber;
    private final int pin;
    private final String name;
    private final int balance;

    public PrincipalDto(int accountNumber, int pin, String name, int balance) {
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.name = name;
        this.balance = balance;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public int getPin() {
        return pin;
    }

    public String getName() {
        return name;
    }

    public int getBalance() {
        return balance;
    }
}
