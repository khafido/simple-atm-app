package org.arkaan.simpleatm.dto.response;

public class AccountDto {

    private final int accountNumber;
    private final String name;

    public AccountDto(int accountNumber, String name) {
        this.accountNumber = accountNumber;
        this.name = name;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public String getName() {
        return name;
    }
}
