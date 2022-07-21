package org.arkaan.simpleatm.util;

public final class Constant {
    public static final int MAX_WITHDRAW_AMOUNT = 1000;
    public static final int MIN_WITHDRAW_AMOUNT = 1;
    public static final int[] AMOUNT_OPTIONS = {10, 50, 100};
    public static final int AMOUNT_MULTIPLE = 10;
    public static final int UNAUTHORIZED = -99;
    public static final String AUTH_FAIL_MESSAGE = "Invalid account number / pin";

    private Constant(){}
}
