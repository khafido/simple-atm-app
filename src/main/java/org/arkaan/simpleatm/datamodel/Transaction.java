package org.arkaan.simpleatm.datamodel;

import java.util.UUID;

public class Transaction {
    private final UUID id;
    private final Type type;
    private final String detail;

    public Transaction(Type type, String detail) {
        this.type = type;
        this.detail = detail;
        id = UUID.randomUUID();
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
}
