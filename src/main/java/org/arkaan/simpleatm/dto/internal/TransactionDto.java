package org.arkaan.simpleatm.dto.internal;

import org.arkaan.simpleatm.model.Account;
import org.arkaan.simpleatm.model.Status;
import org.arkaan.simpleatm.model.Type;

import java.time.LocalDate;

public class TransactionDto {

    private final Type type;
    private final String detail;
    private final Status status;
    private final LocalDate date;
    private final Account account;

    public TransactionDto(Type type, String detail, Status status, LocalDate date, Account account) {
        this.type = type;
        this.detail = detail;
        this.status = status;
        this.date = date;
        this.account = account;
    }

    public Type getType() {
        return type;
    }

    public String getDetail() {
        return detail;
    }

    public Status getStatus() {
        return status;
    }

    public LocalDate getDate() {
        return date;
    }

    public Account getAccount() {
        return account;
    }
}
