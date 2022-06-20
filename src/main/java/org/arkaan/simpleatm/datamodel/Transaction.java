package org.arkaan.simpleatm.datamodel;

public class Transaction {
    
    private static int increment = 0;

    public enum Status {
        SUCCESS,
        FAILED
    }
    
    private final Integer id;
    private final Type type;
    private final String detail;
    private final Status status;
    private final int accountNumber;

    public Transaction(Type type, String detail, Status status, int accountNumber) {
        this.type = type;
        this.detail = detail;
        id = increment++;
        this.status = status;
        this.accountNumber = accountNumber;
    }

    public Integer getId() {
        return id;
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
    
    public int getAccountNumber() {
        return accountNumber;
    }
}
