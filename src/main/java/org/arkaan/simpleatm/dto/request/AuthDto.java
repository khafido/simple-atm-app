package org.arkaan.simpleatm.dto.request;

public class AuthDto {

    private final int accountNumber;
    private final int pin;

    public AuthDto(int accountNumber, int pin) {
        this.accountNumber = accountNumber;
        this.pin = pin;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public int getPin() {
        return pin;
    }
}
