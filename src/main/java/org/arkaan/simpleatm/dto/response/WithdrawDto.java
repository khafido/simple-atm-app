package org.arkaan.simpleatm.dto.response;

public class WithdrawDto {
    private final String date;
    private final int amount;
    private final int balance;

    public WithdrawDto(String date, int amount, int balance) {
        this.date = date;
        this.amount = amount;
        this.balance = balance;
    }

    public String getDate() {
        return date;
    }

    public int getAmount() {
        return amount;
    }

    public int getBalance() {
        return balance;
    }
}
