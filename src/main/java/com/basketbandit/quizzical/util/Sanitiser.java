package com.basketbandit.quizzical.util;

import java.util.Arrays;
import java.util.regex.Pattern;

public class Sanitiser {
    private static final Pattern HEX_PATTERN = Pattern.compile("^\\p{XDigit}+$");

    /**
     * Checks to see if a string is a number or not without the whole Integer.parseInt() exception thang.
     * @param string {@link String}
     * @return boolean
     */
    public static boolean isNumeric(String string) {
        return Arrays.stream(string.split("")).allMatch(character -> Character.isDigit(character.charAt(0)));
    }

    public static boolean isHexadecimal(String string) {
        return HEX_PATTERN.matcher(string).matches();
    }
}
