package com.loopedlabs.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DebugLogDbHelper extends SQLiteOpenHelper {

    private static final String TAG = "DebugLogDbHelper";
    private static final String DATABASE_NAME = "debug_logs.db";
    private static final int DATABASE_VERSION = 1;

    private static SQLiteDatabase database;

    public static SQLiteDatabase getInstance(Context context) {
        if (database == null) {
            synchronized (DebugLogDbHelper.class) {
                new DebugLogDbHelper(context);
            }
        }
        return database;
    }

    private DebugLogDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.initializeDatabase();
    }

    private void initializeDatabase() {
        if (database == null)
            database = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        DebugLogDb.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        DebugLogDb.onUpgrade(db, oldVersion, newVersion);
    }
}
