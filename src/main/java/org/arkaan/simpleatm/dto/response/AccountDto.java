package org.arkaan.simpleatm.dto.response;

public class AccountDto {

    private final int accountNumber;
    private final String name;
    private final int balance;

    public AccountDto(int accountNumber, String name, int balance) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.balance = balance;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public String getName() {
        return name;
    }

    public int getBalance() {
        return balance;
    }
}
