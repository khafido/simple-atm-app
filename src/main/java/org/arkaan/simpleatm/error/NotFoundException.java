package org.arkaan.simpleatm.error;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String e) {
        super(e + " not found.");
    }
}
