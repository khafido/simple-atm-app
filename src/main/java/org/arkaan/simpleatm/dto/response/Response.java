package org.arkaan.simpleatm.dto.response;

import org.arkaan.simpleatm.model.Status;

public class Response<T> {
    private final Status status;
    private final String msg;
    private final T payload;

    public Response(Status status, String msg, T payload) {
        this.status = status;
        this.msg = msg;
        this.payload = payload;
    }

    public String getMsg() {
        return msg;
    }

    public T getPayload() {
        return payload;
    }

    public Status getStatus() {
        return status;
    }
}
