/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2015 Looped Labs Pvt. Ltd.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.loopedlabs.escposprintservicedemo;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.loopedlabs.selector.FileOperation;
import com.loopedlabs.selector.FileSelector;
import com.loopedlabs.selector.OnHandleFileListener;
import com.loopedlabs.util.FileUtils;
import com.loopedlabs.util.TxtFmt;
import com.loopedlabs.util.debug.DebugLog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import static com.loopedlabs.util.FileUtils.readFile;
import static com.loopedlabs.util.FileUtils.readFileAsByteArray;


public class DemoMain extends AppCompatActivity {
    private Context mContext;
    private static String ESC_POS_PRINT_INTENT_ACTION = "com.escpos.intent.action.PRINT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_main);

        initControls();

        isStoragePermissionGranted();
    }

    private void initControls() {
        mContext = this;
        final EditText etPrintCmd = findViewById(R.id.etPrintCmd);
        Button btnPrint = findViewById(R.id.btnPrint);
        final CheckBox cbLf = findViewById(R.id.cbLf);
        final RadioButton rbAscii = findViewById(R.id.rbAscii);

        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = etPrintCmd.getText().toString();
                if (!rbAscii.isChecked()) {
                    if (!s.isEmpty() && (s.length() % 2 == 0)) {
                        PrintCmd(hexStringToByteArray(s));
                    }
                    else {
                        Toast.makeText(DemoMain.this, "Hex data Not Valid", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
                if (!s.isEmpty()) {
                    if (cbLf.isChecked()) {
                        s += "\n";
                    }
                    PrintCmd(s);
                }
                else {
                    Toast.makeText(DemoMain.this, "Enter ASCII data to send to Printer", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.btnPrintPdf).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filePdfSelector();
            }
        });

        findViewById(R.id.btnPrintImg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileImageSelector();
            }
        });

        findViewById(R.id.btnPrintHtml).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileHtmlSelector();
            }
        });

        findViewById(R.id.btnPrintHtml1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileHtml1Selector();
            }
        });

        findViewById(R.id.btnLf).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrintCmd("\n");
            }
        });

        findViewById(R.id.btnPrintReceipt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Generate the receipt
                MyReceipt rcpt = new MyReceipt();
                rcpt.setReceiptHeader1("Bill Header");
                rcpt.setReceiptHeader2("-----------");
                rcpt.setReceiptFooter1("Looped Labs Pvt. Ltd.");
                rcpt.setReceiptFooter2("www.loopedlabs.com");
                rcpt.addLineItem("Item 1", "5.00", "1", "5.00");
                rcpt.addLineItem("Item 2", "12.00", "2", "24.00");
                rcpt.setReceiptTotal("29.00");

                // Generate the Print Buffer
                MyPrinter btp = new MyPrinter(DemoMain.this);
                btp.printLogo();
                btp.setCenterAlign();
                btp.setFontStyleBold();
                btp.printLine(rcpt.getReceiptHeader1());
                btp.printLine(rcpt.getReceiptHeader2());
                btp.printLineFeed();
                btp.printLine(TxtFmt.justify("Name : ", "Customer Name", btp.getMaxLineLength()));
                btp.printDivider('-');
                btp.printLine(rcpt.getsReceiptLineHeader());
                btp.printDivider('-');
                List<MyReceiptLineItem> rli = rcpt.getLineItems();
                for (MyReceiptLineItem li : rli) {
                    btp.printLine(li.getLine());
                }
                btp.printDivider('-');
                btp.printLine(TxtFmt.justify("TOTAL : ", rcpt.getReceiptTotal(), 38));
                btp.printDivider('-');
                btp.setCenterAlign();
                btp.printLine(rcpt.getReceiptFooter1());
                btp.printLine(rcpt.getReceiptFooter2());
                btp.printBlankLines(2);
                btp.setLeftAlign();

                //Print the receipt
                byte[] pb = btp.getPrintData();
                if (!PrintCmd(pb)) {
                    Toast.makeText(DemoMain.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.btnPrintConfig).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager manager = getPackageManager();
                try {
                    Intent i = manager.getLaunchIntentForPackage("com.loopedlabs.escpos.btprintservice");
                    if (i != null) {
                        i.addCategory(Intent.CATEGORY_LAUNCHER);
                        startActivity(i);
                    } else {
                        Toast.makeText(DemoMain.this, "ESC POS BT Print Service not installed", Toast.LENGTH_LONG).show();
                    //    mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.loopedlabs.escposprintservice")));
                    }
                }
                catch (Exception ignored) {
                    Toast.makeText(DemoMain.this, "ESC POS BT Print Service not installed", Toast.LENGTH_LONG).show();
                    //mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.loopedlabs.escposprintservice")));
                }
            }
        });

        findViewById(R.id.tvLoopedLabs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://loopedlabs.com";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }
    private void isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                DebugLog.logTrace("Permission is granted");
            }
            else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            DebugLog.logTrace("Permission is granted by default");
        }
    }
    private FileSelector mFileSel;
    private void fileHtmlSelector() {
        String[] fileFilter = {".html",".htm"};
        mFileSel = new FileSelector(DemoMain.this, FileOperation.LOAD, mHtmlFileListener, fileFilter);
        mFileSel.show();
    }
    private void fileHtml1Selector() {
        String[] fileFilter = {".html",".htm"};
        mFileSel = new FileSelector(DemoMain.this, FileOperation.LOAD, mHtmlFile1Listener, fileFilter);
        mFileSel.show();
    }
    private void fileImageSelector() {
        String[] fileFilter = {".jpg",".jpeg",".png"};
        mFileSel = new FileSelector(DemoMain.this, FileOperation.LOAD, mImageFileListener, fileFilter);
        mFileSel.show();
    }
    private void filePdfSelector() {
        String[] fileFilter = {".pdf"};
        mFileSel = new FileSelector(DemoMain.this, FileOperation.LOAD, mPdfFileListener, fileFilter);
        mFileSel.show();
    }
    private OnHandleFileListener mHtmlFileListener = new OnHandleFileListener() {
        @Override
        public void handleFile(final String selFileName) {
            DebugLog.logTrace("filePath " + selFileName);
            mFileSel.dismiss();
            if (isEscPosPrintServiceAvailable(DemoMain.this, ESC_POS_PRINT_INTENT_ACTION)) {
                File f = new File(selFileName);
                if (f.canRead()) {
                    byte[] bHtml = null;
                    try {
                        bHtml = readFileAsByteArray(f);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    Intent i = new Intent();
                    i.setAction(ESC_POS_PRINT_INTENT_ACTION);
                    //i.putExtra(Intent.EXTRA_TEXT, sHtml);
                    i.putExtra("PRINT_DATA", bHtml);
                    i.putExtra("DATA_TYPE", "HTML");
                    startActivityForResult(i, 2);
                }
            }
            else {
                Toast.makeText(DemoMain.this, "ESC POS BT Print Service not installed", Toast.LENGTH_LONG).show();
                //mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.loopedlabs.escposprintservice")));
            }
        }
    };

    private OnHandleFileListener mHtmlFile1Listener = new OnHandleFileListener() {
        @Override
        public void handleFile(final String selFileName) {
            DebugLog.logTrace("filePath " + selFileName);
            mFileSel.dismiss();
            if (isEscPosPrintServiceAvailable(DemoMain.this, ESC_POS_PRINT_INTENT_ACTION)) {
                File f = new File(selFileName);
                if (f.canRead()) {
                    String sHtml = null;
                    try {
                        sHtml = readFile(f);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.d("sd", sHtml);
                    Intent i = new Intent();
                    i.setAction(ESC_POS_PRINT_INTENT_ACTION);
                    i.putExtra(Intent.EXTRA_TEXT, sHtml);
                    startActivityForResult(i, 2);
                }
            }
            else {
                Toast.makeText(DemoMain.this, "ESC POS BT Print Service not installed", Toast.LENGTH_LONG).show();
                //mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.loopedlabs.escposprintservice")));
            }
        }
    };

    private OnHandleFileListener mHtmlFile1Listener1 = new OnHandleFileListener() {
        @Override
        public void handleFile(final String selFileName) {
            DebugLog.logTrace("filePath " + selFileName);
            mFileSel.dismiss();
            if (isEscPosPrintServiceAvailable(DemoMain.this, ESC_POS_PRINT_INTENT_ACTION)) {
                File f = new File(selFileName);
                if (f.canRead()) {
                    String sHtml = null;
                    try {
                        sHtml = readFile(f);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.d("sd", sHtml);
                    Intent i = new Intent();
                    i.setAction(ESC_POS_PRINT_INTENT_ACTION);
                    i.putExtra(Intent.EXTRA_TEXT, sHtml);
                    startActivityForResult(i, 2);
                }
            }
            else {
                Toast.makeText(DemoMain.this, "ESC POS BT Print Service not installed", Toast.LENGTH_LONG).show();
                //mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.loopedlabs.escposprintservice")));
            }
        }
    };

    private OnHandleFileListener mImageFileListener = new OnHandleFileListener() {
        @Override
        public void handleFile(final String selFileName) {
            DebugLog.logTrace("filePath " + selFileName);
            mFileSel.dismiss();
            if (isEscPosPrintServiceAvailable(DemoMain.this, ESC_POS_PRINT_INTENT_ACTION)) {
                    File f = new File(selFileName);
                if (f.canRead()) {
                    Bitmap bitmap = BitmapFactory.decodeFile(selFileName);
                    ByteArrayOutputStream blob = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0 /* Ignored for PNGs */, blob);
                    byte[] bitmapdata = blob.toByteArray();
                    Intent i = new Intent();
                    i.setAction(ESC_POS_PRINT_INTENT_ACTION);
                    i.putExtra("PRINT_DATA", bitmapdata);
                    i.putExtra("DATA_TYPE", "IMAGE_PNG");
                    startActivityForResult(i, 2);
                }
            }
            else {
                Toast.makeText(DemoMain.this, "ESC POS BT Print Service not installed", Toast.LENGTH_LONG).show();
                //mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.loopedlabs.escposprintservice")));
            }
        }
    };

    private OnHandleFileListener mPdfFileListener = new OnHandleFileListener() {
        @Override
        public void handleFile(final String selFileName) {
            DebugLog.logTrace("filePath " + selFileName);
            mFileSel.dismiss();
            if (isEscPosPrintServiceAvailable(DemoMain.this, ESC_POS_PRINT_INTENT_ACTION)) {
                File f = new File(selFileName);
                if (f.canRead()) {
                    byte[] b = new byte[0];
                    try {
                        b = FileUtils.readFileAsByteArray(f);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    Intent i = new Intent();
                    i.setAction(ESC_POS_PRINT_INTENT_ACTION);
                    i.putExtra("PRINT_DATA", b);
                    i.putExtra("DATA_TYPE", "PDF");
                    startActivityForResult(i, 2);
                }
            }
            else {
                Toast.makeText(DemoMain.this, "ESC POS BT Print Service not installed", Toast.LENGTH_LONG).show();
                //mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.loopedlabs.escposprintservice")));
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.demo_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_about) {
            AlertDialog.Builder aboutBuilder = new AlertDialog.Builder(this);
            aboutBuilder.setTitle(R.string.app_name);
            aboutBuilder
                    .setMessage("App Version : " + BuildConfig.VERSION_CODE + "\nDeveloped By : Looped Labs Pvt. Ltd.\nhttp://loopedlabs.com")
                    .setCancelable(true)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog aboutDialog = aboutBuilder.create();
            aboutDialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean PrintCmd(String s) {
        return !s.isEmpty() && PrintCmd(s.getBytes());

    }

    public boolean PrintCmd(byte[] byteArray) {
        if (byteArray.length <= 0) {
            return false;
        }

        if (isEscPosPrintServiceAvailable(mContext, ESC_POS_PRINT_INTENT_ACTION)) {
            Intent i = new Intent();

            i.setAction(ESC_POS_PRINT_INTENT_ACTION);
            i.putExtra("PRINT_DATA", byteArray);
            startActivityForResult(i, 2);
            return true;
        }
        else {
            Toast.makeText(DemoMain.this, "ESC POS BT Print Service not installed", Toast.LENGTH_LONG).show();
            //mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.loopedlabs.escposprintservice")));
        }
        return false;
    }

    public boolean isEscPosPrintServiceAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List resolveInfo =
                packageManager.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        return resolveInfo.size() > 0;
    }

    private byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
}
