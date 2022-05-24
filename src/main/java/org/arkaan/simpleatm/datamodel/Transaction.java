package org.arkaan.simpleatm.datamodel;

import java.util.UUID;

public class Transaction {

    public enum Status {
        SUCCESS,
        FAILED
    }
    private final UUID id;
    private final Type type;
    private final String detail;
    private final Status status;

    public Transaction(Type type, String detail, Status status) {
        this.type = type;
        this.detail = detail;
        id = UUID.randomUUID();
        this.status = status;
    }

    public UUID getId() {
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
}
