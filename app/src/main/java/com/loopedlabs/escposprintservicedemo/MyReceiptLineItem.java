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

import com.loopedlabs.util.TxtFmt;

import java.util.Arrays;

public class MyReceiptLineItem {
    private String sLineItem;
    private String sItemRate;
    private String sItemQty;
    private String sItemTotal;

    public MyReceiptLineItem() {
        sLineItem = "";
        sItemRate = "";
        sItemQty = "";
        sItemTotal = "";
    }

    public MyReceiptLineItem(String sLineItem, String sItemRate, String sItemQty, String sItemTotal) {
        int iMaxLen = 19;
        if (sLineItem.length() > iMaxLen) {
            this.sLineItem = sLineItem.substring(0,iMaxLen);
        } else {
            int spacer = iMaxLen - sLineItem.length();
            char[] chars = new char[spacer];
            Arrays.fill(chars, ' ');
            String s = new String(chars);
            this.sLineItem = sLineItem + s;
        }
        if (sItemRate.length() > 7) {
            this.sItemRate = " " + sItemRate.substring(7 - sItemRate.length());
        } else {
            this.sItemRate = TxtFmt.rightAlign(sItemRate, 8);
        }

        if (sItemQty.length() > 7) {
            this.sItemQty = " " + sItemRate.substring(7 - sItemRate.length());
        } else {
            this.sItemQty = TxtFmt.centerAlign(sItemQty, 8);
        }

        this.sItemTotal = TxtFmt.rightAlign(sItemTotal,7);
    }

    public String getLineItem() {
        return sLineItem;
    }

    public String getLine() {
        return (sLineItem + sItemRate + sItemQty + sItemTotal);
    }

    public void setLineItem(String sLineItem) {
        this.sLineItem = sLineItem;
    }

    public String getItemRate() {
        return sItemRate;
    }

    public void setItemRate(String sItemRate) {
        this.sItemRate = sItemRate;
    }

    public String getItemQty() {
        return sItemQty;
    }

    public void setItemQty(String sItemQty) {
        this.sItemQty = sItemQty;
    }

    public String getItemTotal() {
        return sItemTotal;
    }

    public void setItemTotal(String sItemTotal) {
        this.sItemTotal = sItemTotal;
    }
}
