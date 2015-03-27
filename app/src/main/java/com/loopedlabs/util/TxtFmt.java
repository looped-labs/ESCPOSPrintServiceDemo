package com.loopedlabs.util;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class TxtFmt {
    public static String justify(String start, String end, int maxLineLength) {

        String newString = "";
        int startLength = start.length();

        int endLength = end.length();
        int remainLength = maxLineLength - (startLength + endLength);
        for (int i = 0; i < startLength; i++) {
            newString += start.charAt(i);
        }

        for (int s = 0; s < remainLength; s++) {
            newString += " ";
        }

        for (int j = 0; j < endLength; j++) {
            newString += end.charAt(j);
        }

        return newString;
    }

    public static String rightAlign(String str,  int maxLineLength) {
        String s = "";
        if (str == null || str.isEmpty()) {
            return s;
        }

        int spacer = maxLineLength - str.length();
        if (spacer > 0) {
            char[] chars = new char[spacer];
            Arrays.fill(chars, ' ');
            s = new String(chars);
            return s + str;
        }
        if (spacer < 0) {
            spacer *= -1;
            return str.substring(spacer);
        }
        return str;
    }

    public static String centerAlign(String txt,  int maxLineLength) {
        String cTxt = "";

        if (txt == null || txt.isEmpty()) {
            return cTxt;
        }

        int idx = 0;
        while (txt.length() > maxLineLength) {
            cTxt += txt.substring(idx, idx + maxLineLength) + "\n";
            txt = txt.substring(idx + maxLineLength);
        }

        if (txt.length() == maxLineLength) {
            cTxt += txt;
        } else {
            int spacer = (maxLineLength - txt.length()) / 2;
            if ((spacer * 2) + txt.length() > maxLineLength) {
                spacer--;
            }
            char[] chars = new char[spacer];
            Arrays.fill(chars, ' ');
            String s = new String(chars);
            cTxt += s + txt;
        }
        return cTxt;
    }

    public static String printDate() {
        Date date = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("E',' dd MMM y hh:mm:ss a", Locale.ENGLISH);
        return ft.format(date);
    }
}
