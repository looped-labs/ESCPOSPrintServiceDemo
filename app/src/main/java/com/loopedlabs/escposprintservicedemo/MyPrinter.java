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

import java.util.ArrayList;
import java.util.List;

public class MyPrinter {
    List<Byte> lPrintData = new ArrayList<>();
    Context mContext;

    private static byte[] BTP_PRINT_LOGO = new byte[]{(byte) 0x1B,
            (byte) 0x2F, (byte) 0x00};
    private static byte[] BTPC_PRINT_LINE_FEED = new byte[]{(byte) 0x0A};

    private static byte[] BTP_SET_FONT_SIZE_NORMAL = new byte[]{(byte) 0x1B,
            (byte) 0x21, (byte) 0x00};
    private static byte[] BTP_SET_FONT_STYLE_BOLD = new byte[]{(byte) 0x1B,
            (byte) 0x74, (byte) 0x01};
    private static byte[] BTP_SET_FONT_SIZE_DOUBLE_HEIGHT = new byte[]{
            (byte) 0x1B, (byte) 0x21, (byte) 0x0F};
    private static byte[] BTP_SET_FONT_SIZE_DOUBLE_WIDTH = new byte[]{
            (byte) 0x1B, (byte) 0x21, (byte) 0xF0};
    private static byte[] BTP_SET_FONT_SIZE_DOUBLE_W_H = new byte[]{
            (byte) 0x1B, (byte) 0x21, (byte) 0xFF};
    private static byte[] BTP_SET_LEFT_ALIGN = new byte[]{
            (byte) 0x1B, (byte) 0x10, (byte) 0x00};
    private static byte[] BTP_SET_CENTER_ALIGN = new byte[]{
            (byte) 0x1B, (byte) 0x10, (byte) 0x01};
    private static byte[] BTP_SET_FONT_KANNADA = new byte[]{(byte) 0x1B,
            (byte) 0x74, (byte) 0x0A};

    //2 Inch Printer char per line
    private static final int NORMAL_LINE_WIDTH = 42;
    private static final int BOLD_LINE_WIDTH = 38;
    private static final int DOUBLE_LINE_W_H = 19;
    private static final int DOUBLE_LINE_H = 40;
    private static final int DOUBLE_LINE_W = 20;

    private int maxLineLength;

     public MyPrinter(Context mContext) {
         maxLineLength = NORMAL_LINE_WIDTH;
         this.mContext = mContext;
    }

    public void ClearPrintData() {
        lPrintData.clear();
    }

    public byte[] getPrintData() {
        byte[] pb = new byte[lPrintData.size()];
        for (int x = 0; x < lPrintData.size(); x++) {
            pb[x] = lPrintData.get(x);
        }
        return pb;
    }

    public boolean printLogo() {
        for (byte b : BTP_PRINT_LOGO) {
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

    public boolean setFontStyleNormal() {
        for (byte b : BTP_SET_FONT_SIZE_NORMAL) {
            lPrintData.add(b);
        }
        maxLineLength = NORMAL_LINE_WIDTH;
        return true;
    }

    public boolean setFontStyleBold() {
        for (byte b : BTP_SET_FONT_STYLE_BOLD) {
            lPrintData.add(b);
        }
        maxLineLength = BOLD_LINE_WIDTH;
        return true;
    }

    public boolean setFontSizeDoubleHeight() {
        for (byte b : BTP_SET_FONT_SIZE_DOUBLE_HEIGHT) {
            lPrintData.add(b);
        }
        maxLineLength = DOUBLE_LINE_H;
        return true;
    }

    public boolean setFontSizeDoubleWidth() {
        for (byte b : BTP_SET_FONT_SIZE_DOUBLE_WIDTH) {
            lPrintData.add(b);
        }
        maxLineLength = DOUBLE_LINE_H;
        return true;
    }

    public boolean setFontSizeDoubleWidthAndHeight() {
        for (byte b : BTP_SET_FONT_SIZE_DOUBLE_W_H) {
            lPrintData.add(b);
        }
        maxLineLength = DOUBLE_LINE_W_H;
        return true;
    }

    public boolean setLeftAlign() {
        for (byte b : BTP_SET_LEFT_ALIGN) {
            lPrintData.add(b);
        }
        return true;
    }

    public boolean setCenterAlign() {
        for (byte b : BTP_SET_CENTER_ALIGN) {
            lPrintData.add(b);
        }
        return true;
    }
//    public boolean setKannadaFont() {
//        for (byte b : BTP_SET_FONT_KANNADA) {
//            lPrintData.add(b);
//        }
//        return true;
//    }

    public boolean printLine(String txt) {
        if (txt.length() <= 0) {
            return false;
        }
        printText(txt);
        for (byte a : BTPC_PRINT_LINE_FEED) {
            lPrintData.add(a);
        }
        return true;
    }

    public boolean printDivider(char d) {
        String divider = "";
        for (int i = 0; i < maxLineLength; i++) {
            divider += d;
        }

        byte[] data = divider.getBytes();
        for (byte b : data) {
            lPrintData.add(b);
        }
        for (byte a : BTPC_PRINT_LINE_FEED) {
            lPrintData.add(a);
        }
        return true;
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

    public void printBlankLines(int num) {
        for (int i = 0; i < num; i++)
            printLineFeed();
    }

    public int getMaxLineLength() {
        return maxLineLength;
    }
}
