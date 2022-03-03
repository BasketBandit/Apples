package com.basketbandit.words.util;

import java.util.Arrays;

public class Sanitiser {
    /**
     * Checks to see if a string is a number or not without the whole Integer.parseInt() exception thang.
     * @param string {@link String}
     * @return boolean
     */
    public static boolean isNumeric(String string) {
        return Arrays.stream(string.split("")).allMatch(character -> Character.isDigit(character.charAt(0)));
    }
}
