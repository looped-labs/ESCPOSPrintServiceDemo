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

import java.util.ArrayList;
import java.util.List;

public class MyReceipt {
    private String sReceiptHeader1;
    private String sReceiptHeader2;
    private String sReceiptFooter1;
    private String sReceiptFooter2;
    private String sReceiptLineHeader;
    private List<MyReceiptLineItem> lineItems;
    private String sReceiptTotal;

    public MyReceipt() {
        lineItems = new ArrayList<>();
                            //12345678901234567890123456789012345678
        sReceiptLineHeader = "Item Name             Rate   Qty Total";
    }

    public String getReceiptHeader1() {
        return sReceiptHeader1;
    }

    public void setReceiptHeader1(String sReceiptHeader1) {
        this.sReceiptHeader1 = sReceiptHeader1;
    }

    public String getReceiptHeader2() {
        return sReceiptHeader2;
    }

    public void setReceiptHeader2(String sReceiptHeader2) {
        this.sReceiptHeader2 = sReceiptHeader2;
    }

    public String getReceiptFooter1() {
        return sReceiptFooter1;
    }

    public void setReceiptFooter1(String sReceiptFooter1) {
        this.sReceiptFooter1 = sReceiptFooter1;
    }

    public String getReceiptFooter2() {
        return sReceiptFooter2;
    }

    public void setReceiptFooter2(String sReceiptFooter2) {
        this.sReceiptFooter2 = sReceiptFooter2;
    }

    public String getReceiptLineHeader() {
        return sReceiptLineHeader;
    }

    public String getsReceiptLineHeader() {
        return sReceiptLineHeader;
    }

    public List<MyReceiptLineItem> getLineItems() {
        return lineItems;
    }

    public void addLineItem(String sLineItem, String sItemRate, String sItemQty, String sItemTotal) {
        MyReceiptLineItem li = new MyReceiptLineItem(sLineItem, sItemRate, sItemQty, sItemTotal);
        lineItems.add(li);
    }

    public String getReceiptTotal() {
        return sReceiptTotal;
    }

    public void setReceiptTotal(String sReceiptTotal) {
        this.sReceiptTotal = sReceiptTotal;
    }
}
