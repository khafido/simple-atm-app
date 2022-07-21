package org.arkaan.simpleatm.error;

import org.arkaan.simpleatm.util.Constant;

public class AuthenticationException extends RuntimeException {

    public AuthenticationException() {
        super(Constant.AUTH_FAIL_MESSAGE);
    }
}
