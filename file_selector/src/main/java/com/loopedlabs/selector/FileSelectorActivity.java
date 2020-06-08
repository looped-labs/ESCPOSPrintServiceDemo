package com.loopedlabs.selector;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


public class FileSelectorActivity extends Activity {

    private Button mLoadButton;

    private Button mSaveButton;

    /**
     * Sample filters array
     */
    final String[] mFileFilter = {"*.*", ".jpeg", ".txt", ".png"};

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_selector_main);

        mLoadButton =  findViewById(R.id.button_load);
        mSaveButton = findViewById(R.id.button_save);

        mLoadButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                new FileSelector(FileSelectorActivity.this, FileOperation.LOAD, mLoadFileListener, mFileFilter).show();
            }
        });

        mSaveButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                new FileSelector(FileSelectorActivity.this, FileOperation.SAVE, mSaveFileListener, mFileFilter).show();
            }
        });
    }

    OnHandleFileListener mLoadFileListener = new OnHandleFileListener() {
        @Override
        public void handleFile(final String filePath) {
            Toast.makeText(FileSelectorActivity.this, "Load: " + filePath, Toast.LENGTH_SHORT).show();
        }
    };

    OnHandleFileListener mSaveFileListener = new OnHandleFileListener() {
        @Override
        public void handleFile(final String filePath) {
            Toast.makeText(FileSelectorActivity.this, "Save: " + filePath, Toast.LENGTH_SHORT).show();
        }
    };

}