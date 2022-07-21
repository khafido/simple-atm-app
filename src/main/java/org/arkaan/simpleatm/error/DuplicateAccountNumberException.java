package org.arkaan.simpleatm.error;

public class DuplicateAccountNumberException extends RuntimeException {

    private static final long serialVersionUID = -6376610883488247045L;
    private final Integer accountNumber;
    
    public DuplicateAccountNumberException(Integer accountNumber) {
        this.accountNumber = accountNumber;
    }
    
    @Override
    public String getMessage() {
        return String.format(
                "There can't be 2 different accounts with the same Account Number: %d", 
                accountNumber);
    }
}
