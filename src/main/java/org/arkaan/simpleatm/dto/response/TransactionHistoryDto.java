package org.arkaan.simpleatm.dto.response;

import org.arkaan.simpleatm.model.Status;
import org.arkaan.simpleatm.model.Type;

public class TransactionHistoryDto {

    private final int id;
    private final Type type;
    private final Status status;
    private final String detail;

    public TransactionHistoryDto(int id, Type type, Status status, String detail) {
        this.id = id;
        this.type = type;
        this.status = status;
        this.detail = detail;
    }

    public int getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public Status getStatus() {
        return status;
    }

    public String getDetail() {
        return detail;
    }
}
