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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class DemoMain extends ActionBarActivity {
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_main);

        initControls();
    }

    private void initControls() {
        mContext = this;
        final EditText etPrintCmd = (EditText)findViewById(R.id.etPrintCmd);
        Button btnPrint = (Button) findViewById(R.id.btnPrint);
        final CheckBox cbLf = (CheckBox)findViewById(R.id.cbLf);
        final RadioButton rbAscii = (RadioButton)findViewById(R.id.rbAscii);

        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = etPrintCmd.getText().toString();
                if (!rbAscii.isChecked()) {
                    if (!s.isEmpty() && (s.length()%2 == 0)) {
                        PrintCmd(hexStringToByteArray(s));
                    } else {
                        Toast.makeText(DemoMain.this,"Hex data Not Valid",Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
                if (!s.isEmpty()) {
                    if (cbLf.isChecked()) {
                        s+="\n";
                    }
                    PrintCmd(s);
                } else {
                    Toast.makeText(DemoMain.this,"Enter ASCII data to send to Printer",Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btnLf = (Button) findViewById(R.id.btnLf);
        btnLf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrintCmd("\n");
            }
        });

        Button btnCrLf = (Button) findViewById(R.id.btnCrLf);
        btnCrLf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrintCmd("\r\n");
            }
        });

        Button btnPrintConfig = (Button)findViewById(R.id.btnPrintConfig);
        btnPrintConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager manager = getPackageManager();
                try {
                    Intent i = manager.getLaunchIntentForPackage("com.loopedlabs.escposprintservice");
                    if (i != null) {
                        i.addCategory(Intent.CATEGORY_LAUNCHER);
                        startActivity(i);
                    }
                } catch (Exception e) {

                }
            }
        });

        TextView tvLoopedLabs = (TextView) findViewById(R.id.tvLoopedLabs);
        tvLoopedLabs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://loopedlabs.com";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }

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
        if (s.isEmpty()) {
            return false;
        }

        return PrintCmd(s.getBytes());
    }
    public boolean PrintCmd(byte[] byteArray) {
        if (byteArray.length <= 0) {
            return false;
        }

        if (isIntentAvailable(mContext, "org.escpos.intent.action.PRINT")) {
            Intent i = new Intent();

            i.setAction("org.escpos.intent.action.PRINT");
            i.putExtra("PRINT_DATA", byteArray);
            startActivityForResult(i, 2);
            return true;
        } else {
            String appPackageName = "com.loopedlabs.escposprintservice";
            try {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException e) {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
            return false;
        }
    }

    public boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List resolveInfo =
                packageManager.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        if (resolveInfo.size() > 0) {
            return true;
        }
        return false;
    }

    private byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
}
