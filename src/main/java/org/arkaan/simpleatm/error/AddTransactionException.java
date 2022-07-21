package org.arkaan.simpleatm.error;

public class AddTransactionException extends RuntimeException {

    public AddTransactionException() {
        super("Something went wrong.");
    }
}
