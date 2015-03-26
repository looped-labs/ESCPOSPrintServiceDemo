/**
 The MIT License (MIT)

 Copyright (c) 2015 Looped Labs Pvt. Ltd.

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 */
package com.loopedlabs.escposprintservicedemo;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyPrinter {
    List<Byte> lPrintData = new ArrayList<Byte>();
    Context mContext;

    private static byte[] BTPC_PRINT_LOGO = new byte[]{(byte) 0x1B,
            (byte) 0x2F, (byte) 0x00};
    private static byte[] BTPC_PRINT_LINE_FEED = new byte[]{(byte) 0x0A};

    private static byte[] BTPC_SET_FONT_SIZE_NORMAL = new byte[]{(byte) 0x1B,
            (byte) 0x21, (byte) 0x00};
    private static byte[] BTPC_SET_FONT_STYLE_BOLD = new byte[]{(byte) 0x1B,
            (byte) 0x74, (byte) 0x01};
    private static byte[] BTPC_SET_FONT_SIZE_DOUBLE_HEIGHT = new byte[]{
            (byte) 0x1B, (byte) 0x21, (byte) 0x0F};
    private static byte[] BTPC_SET_FONT_SIZE_DOUBLE_WIDTH = new byte[]{
            (byte) 0x1B, (byte) 0x21, (byte) 0xF0};
    private static byte[] BTPC_SET_FONT_SIZE_DOUBLE_W_H = new byte[]{
            (byte) 0x1B, (byte) 0x21, (byte) 0xFF};
    private static byte[] BTPC_SET_ALIGNMENT_LEFT = new byte[]{
            (byte) 0x1B, (byte) 0x10, (byte) 0x00};
    private static byte[] BTPC_SET_ALIGNMENT_RIGHT = new byte[]{
            (byte) 0x1B, (byte) 0x10, (byte) 0x01};
    private static byte[] BTPC_SET_FONT_KANNADA = new byte[]{(byte) 0x1B,
            (byte) 0x74, (byte) 0x0A};

    private String divider = "";
    //2 Inch Printer char lengths
    private static final int NORMAL_LINE_WIDTH = 42;
    private static final int BOLD_LINE_WIDTH = 38;
    private static final int DOUBLE_LINE_W_H = 19;
    private static final int DOUBLE_LINE_H = 40;
    private static final int DOUBLE_LINE_W = 20;
    private static int maxLineLength = NORMAL_LINE_WIDTH;

    public MyPrinter(Context mContext) {
        this.mContext = mContext;
    }

    public void ClearPrintData() {
        lPrintData.clear();
    }

    public List<Byte> getPrintData() {
        return lPrintData;
    }

    public boolean printLogo() {
        for (byte b : BTPC_PRINT_LOGO) {
            lPrintData.add(b);
        }
        return true;
    }

    public boolean printLineFeed() {
        for (byte b : BTPC_PRINT_LINE_FEED) {
            lPrintData.add(b);
        }
        return true;
    }

    public boolean fontSizeNormal() {
        for (byte b : BTPC_SET_FONT_SIZE_NORMAL) {
            lPrintData.add(b);
        }
        maxLineLength = NORMAL_LINE_WIDTH;
        return true;
    }

    public boolean fontSizeBold() {
        for (byte b : BTPC_SET_FONT_STYLE_BOLD) {
            lPrintData.add(b);
        }
        maxLineLength = BOLD_LINE_WIDTH;
        return true;
    }

    public boolean fontSizeDouble_h() {
        for (byte b : BTPC_SET_FONT_SIZE_DOUBLE_HEIGHT) {
            lPrintData.add(b);
        }
        maxLineLength = DOUBLE_LINE_H;
        return true;
    }

    public boolean fontSizeDouble_w() {
        for (byte b : BTPC_SET_FONT_SIZE_DOUBLE_WIDTH) {
            lPrintData.add(b);
        }
        maxLineLength = DOUBLE_LINE_H;
        return true;
    }

    public boolean fontSizeDouble_w_h() {
        for (byte b : BTPC_SET_FONT_SIZE_DOUBLE_W_H) {
            lPrintData.add(b);
        }
        maxLineLength = DOUBLE_LINE_W_H;
        return true;
    }

    public boolean setAlignmentRight() {
        for (byte b : BTPC_SET_ALIGNMENT_RIGHT) {
            lPrintData.add(b);
        }
        return true;
    }

    public boolean setAlignmentRight(String txt) {
        for (byte b : BTPC_SET_ALIGNMENT_RIGHT) {
            lPrintData.add(b);
        }
        return true;
    }

    public boolean setAlignmentLeft() {
        for (byte b : BTPC_SET_ALIGNMENT_LEFT) {
            lPrintData.add(b);
        }
        return true;
    }

    public boolean setKannadaFont() {
        for (byte b : BTPC_SET_FONT_KANNADA) {
            lPrintData.add(b);
        }
        return true;
    }

    public boolean printLineFeed(String txt) {
        if (txt.length() <= 0) {
            return false;
        }
        printText(txt);
        for (byte a : BTPC_PRINT_LINE_FEED) {
            lPrintData.add(a);
        }
        return true;
    }

    public boolean setMaxLineLength(int v) {
        maxLineLength = v;
        return true;
    }

    public String setDivider() {
        divider = "";
        for (int i = 0; i < maxLineLength; i++) {
            divider += "-";
        }
        return divider;
    }

    public String justify(String start, String end) {

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

    public String rightAlign(String str) {
        int spacer = maxLineLength - str.length();
        if (spacer + str.length() > maxLineLength) {
            spacer--;
        }

        char[] chars = new char[spacer];
        Arrays.fill(chars, ' ');
        String s = new String(chars);
        return s + str;
    }

    public boolean printText(String txt) {
        if (txt.length() <= 0) {
            return false;
        }
        byte[] data = txt.getBytes();
        for (byte b : data) {
            lPrintData.add(b);
        }
        return true;
    }

    public void printLines(int num) {
        for (int i = 0; i < num; i++)
            printLineFeed();
    }

    public String printDate() {
        Date date = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("E',' dd MMM y hh:mm:ss a", Locale.ENGLISH);
        return ft.format(date);
    }

    public String centerAlign(String txt) {
        String cTxt = "";

        if (txt == null || txt.length() <= 0) {
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
}
