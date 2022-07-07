package org.arkaan.simpleatm.util;

public final class Helper {
    public static boolean isNotNumber(String s) {
        return !s.matches("^[0-9]+");
    }

    public static boolean isSixDigits(String s) {
        return s.length() == 6;
    }


    private Helper() {}
}
