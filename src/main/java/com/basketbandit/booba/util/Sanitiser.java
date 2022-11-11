package com.basketbandit.booba.util;

import java.util.Arrays;
import java.util.regex.Pattern;

public class Sanitiser {
    private static final Pattern HEX_PATTERN = Pattern.compile("^\\p{XDigit}+$");

    /**
     * Checks to see if the input is a number, without the whole Integer.parseInt() exception flow.
     * @param string {@link String}
     * @return boolean
     */
    public static boolean isNumeric(String string) {
        return Arrays.stream(string.split("")).allMatch(character -> Character.isDigit(character.charAt(0)));
    }

    /**
     * Checks to see if the input is a valid hexadecimal string.
     * @param string {@link String}
     * @return boolean
     */
    public static boolean isHexadecimal(String string) {
        return HEX_PATTERN.matcher(string).matches();
    }
}
