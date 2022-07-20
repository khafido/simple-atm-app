package org.arkaan.simpleatm.dto;

import static org.arkaan.simpleatm.model.Transaction.Status;

public class Response {
    private final Status status;
    private final String msg;
    private final Object payload;

    public Response(Status status, String msg, Object payload) {
        this.status = status;
        this.msg = msg;
        this.payload = payload;
    }

    public String getMsg() {
        return msg;
    }

    public Object getPayload() {
        return payload;
    }

    public Status getStatus() {
        return status;
    }
}
