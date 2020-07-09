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
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.loopedlabs.selector.FileOperation;
import com.loopedlabs.selector.FileSelector;
import com.loopedlabs.selector.OnHandleFileListener;
import com.loopedlabs.util.debug.DebugLog;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


public class DemoMain extends AppCompatActivity {
    private static final String ESC_POS_PRINT_INTENT_ACTION = "org.escpos.intent.action.PRINT";
    private static final String ESC_POS_BLUETOOTH_PRINT_SERVICE = "com.loopedlabs.escposprintservice";
    private static final String ESC_POS_WIFI_PRINT_SERVICE = "com.loopedlabs.netprintservice";
    private static final String ESC_POS_USB_PRINT_SERVICE = "com.loopedlabs.usbprintservice";
    private int iSelPrintService = 0;
    private int iSelPrintUrlType = 0;
    private int iSelPrintFileType = 0;
    private String sAppPackage = "com.loopedlabs.escposprintservice";
    private Spinner spPrintService;
    private String sPrintUrl = "";
    private String sPrintFile = "";
    private String sDataType = "";
    private String sFileType = "";
    private TextView tvPrintFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_main);

        DebugLog.setDebugMode(BuildConfig.DEBUG);
        DebugLog.logTrace();

        initControls();

        isStoragePermissionGranted();
    }

    private void initControls() {
        DebugLog.logTrace();
        iSelPrintService = 0;
        sAppPackage = ESC_POS_BLUETOOTH_PRINT_SERVICE;
        spPrintService = findViewById(R.id.spPrintService);
        spPrintService.setTag("a");
        spPrintService.setSelection(iSelPrintService, false);
        spPrintService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!spPrintService.getTag().equals("a")) {
                    iSelPrintService = position;
                    switch (iSelPrintService) {
                        case 0:
                            sAppPackage = ESC_POS_BLUETOOTH_PRINT_SERVICE;
                            break;
                        case 1:
                            sAppPackage = ESC_POS_WIFI_PRINT_SERVICE;
                            break;
                        case 2:
                            sAppPackage = ESC_POS_USB_PRINT_SERVICE;
                            break;
                    }
                    alert("Selected Print Service : " + spPrintService.getItemAtPosition(position).toString());
                }
                else {
                    spPrintService.setTag("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        findViewById(R.id.btnPrintConfig).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager manager = getPackageManager();
                try {
                    Intent i = manager.getLaunchIntentForPackage(sAppPackage);
                    if (i != null) {
                        i.addCategory(Intent.CATEGORY_LAUNCHER);
                        startActivity(i);
                    }
                    else {
                        requestInstallPrintService();
                    }
                }
                catch (Exception ignored) {
                    requestInstallPrintService();
                }
            }
        });

        final EditText etPrintUrl = findViewById(R.id.etPrintUrl);

        final Spinner spPrintURLType = findViewById(R.id.spPrintURLType);
        iSelPrintUrlType = 0;
        sPrintUrl = "https://loopedlabs.com/web-print/loopedlabs.png";
        sDataType = "PNG_URL";
        spPrintURLType.setSelection(iSelPrintUrlType, false);
        spPrintURLType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                iSelPrintUrlType = position;
                sPrintUrl = etPrintUrl.getText().toString();
                if (sPrintUrl.isEmpty() || sPrintUrl.contains("loopedlabs")) {
                    switch (iSelPrintUrlType) {
                        case 0:
                            sPrintUrl = "https://loopedlabs.com/web-print/loopedlabs.png";
                            sDataType = "PNG_URL";
                            break;
                        case 1:
                            sPrintUrl = "https://loopedlabs.com/web-print/loopedlabs.jpg";
                            sDataType = "JPG_URL";
                            break;
                        case 2:
                            sPrintUrl = "https://loopedlabs.com/web-print/sample.pdf";
                            sDataType = "PDF_URL";
                            break;
                        case 3:
                            sPrintUrl = "https://loopedlabs.com/web-print/bill.html";
                            sDataType = "HTML_URL";
                            break;
                    }
                    etPrintUrl.setText(sPrintUrl);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        findViewById(R.id.btnPrintUrl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sPrintUrl = etPrintUrl.getText().toString();
                if (!URLUtil.isNetworkUrl(sPrintUrl)) {
                    etPrintUrl.requestFocus();
                    alert("Please enter a valid absolute URL");
                }
                else {
                    hideKeyboard(DemoMain.this);
                    Intent printIntent = new Intent(ESC_POS_PRINT_INTENT_ACTION);
                    printIntent.setPackage(sAppPackage);
                    printIntent.putExtra("DATA_TYPE", sDataType);
                    printIntent.putExtra(Intent.EXTRA_TEXT, sPrintUrl);
                    DebugLog.logTrace("Print Intent");
                    DebugLog.logTrace("Package   : " + sAppPackage);
                    DebugLog.logTrace("Data Type : " + sDataType);
                    DebugLog.logTrace("Print Url : " + sPrintUrl);
                    startActivity(printIntent);
                }
            }
        });

        tvPrintFile = findViewById(R.id.tvPrintFile);
        final Spinner spPrintFileType = findViewById(R.id.spPrintFileType);
        iSelPrintFileType = 0;
        sPrintFile = "";
        sFileType = "IMAGE_PNG";
        spPrintFileType.setSelection(iSelPrintFileType, false);
        spPrintFileType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                iSelPrintFileType = position;
                sPrintFile = "";
                switch (iSelPrintFileType) {
                    case 0:
                        sFileType = "IMAGE_PNG";
                        break;
                    case 1:
                        sFileType = "IMAGE_JPG";
                        break;
                    case 2:
                        sFileType = "PDF";
                        break;
                    case 3:
                        sFileType = "HTML";
                        break;
                }
                tvPrintFile.setText(sPrintFile);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        findViewById(R.id.btnBrowse).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileSelector();
            }
        });

        findViewById(R.id.tvLoopedLabs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://loopedlabs.com";
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

    private void fileSelector() {
        String[] fileHtmlFilter = {".html", ".htm"};
        String[] fileJpgFilter = {".jpg", ".jpeg"};
        String[] filePngFilter = {".png"};
        String[] filePdfFilter = {".pdf"};
        switch (sFileType) {
            case "IMAGE_PNG":
                mFileSel = new FileSelector(DemoMain.this, FileOperation.LOAD, mFileListener, filePngFilter);
                break;
            case "IMAGE_JPG":
                mFileSel = new FileSelector(DemoMain.this, FileOperation.LOAD, mFileListener, fileJpgFilter);
                break;
            case "PDF":
                mFileSel = new FileSelector(DemoMain.this, FileOperation.LOAD, mFileListener, filePdfFilter);
                break;
            case "HTML":
                mFileSel = new FileSelector(DemoMain.this, FileOperation.LOAD, mFileListener, fileHtmlFilter);
                break;
        }
        mFileSel.show();
    }

    private OnHandleFileListener mFileListener = new OnHandleFileListener() {
        @Override
        public void handleFile(final String selFileName) {
            DebugLog.logTrace("filePath " + selFileName);
            mFileSel.dismiss();
            tvPrintFile.setText(selFileName);
            if (isEscPosPrintServiceAvailable()) {
                File f = new File(selFileName);
                if (f.canRead()) {
                    byte[] bytes = null;
                    try {
                        bytes = readFileAsByteArray(f);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                        alert("Unable to read file contents");
                    }

                    Intent i = new Intent();
                    i.setAction(ESC_POS_PRINT_INTENT_ACTION);
                    i.setPackage(sAppPackage);
                    i.putExtra("PRINT_DATA", bytes);
                    i.putExtra("DATA_TYPE", sFileType);
                    DebugLog.logTrace("Print Intent");
                    DebugLog.logTrace("Package   : " + sAppPackage);
                    DebugLog.logTrace("Data Type : " + sFileType);
                    DebugLog.logTrace("File Name : " + selFileName);
                    startActivity(i);
                }
                else {
                    alert("Unable to read file");
                }
            }
            else {
                requestInstallPrintService();
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
            alert("App Version : " + BuildConfig.VERSION_CODE + "\nDeveloped By : Looped Labs Pvt. Ltd.\nhttps://loopedlabs.com");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isEscPosPrintServiceAvailable() {
        DebugLog.logTrace();
        final PackageManager packageManager = getPackageManager();
        final Intent intent = new Intent(ESC_POS_PRINT_INTENT_ACTION);
        intent.setPackage(sAppPackage);
        List<ResolveInfo> resolveInfo =
                packageManager.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        return resolveInfo.size() > 0;
    }

    private static void hideKeyboard(Activity activity) {
        DebugLog.logTrace();
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm != null) {
            View view = activity.getCurrentFocus();
            if (view == null) {
                view = new View(activity);
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void requestInstallPrintService() {
        DebugLog.logTrace();
        AlertDialog.Builder aboutBuilder = new AlertDialog.Builder(this);
        aboutBuilder.setTitle(R.string.app_name);
        aboutBuilder
                .setMessage(spPrintService.getItemAtPosition(iSelPrintService).toString() + " not installed, do you want to install the print service from Google Play Store ?")
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent intentPlayStore = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + sAppPackage));
                        startActivity(intentPlayStore);
                    }
                })
                .setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog aboutDialog = aboutBuilder.create();
        aboutDialog.show();
    }

    private void alert(String sMsg) {
        DebugLog.logTrace();
        AlertDialog.Builder aboutBuilder = new AlertDialog.Builder(this);
        aboutBuilder.setTitle(R.string.app_name);
        aboutBuilder
                .setMessage(sMsg)
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog aboutDialog = aboutBuilder.create();
        aboutDialog.show();
    }

    private static byte[] readFileAsByteArray(File f) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(f, "r");
        byte[] bytes = new byte[(int) raf.length()];
        raf.readFully(bytes);
        raf.close();
        return bytes;
    }


}
