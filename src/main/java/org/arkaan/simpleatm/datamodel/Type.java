package org.arkaan.simpleatm.datamodel;

public enum Type {
    BALANCE("Balance inquiry"),
    DEPOSIT("Deposit money"),
    WITHDRAWAL("Perform withdrawal"),
    TRANSFER("Transfer money");

    private final String desc;

    Type(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
