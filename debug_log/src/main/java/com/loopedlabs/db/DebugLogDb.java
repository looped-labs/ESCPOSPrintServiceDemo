package com.loopedlabs.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.loopedlabs.beans.DbgLog;

import java.util.ArrayList;
import java.util.List;

public class DebugLogDb {
    private static final String TAG = "DebugLogDb";

    private static final String TABLE_NAME = "debug_logs";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_LOG_DATA = "log_data";
    private static final String COLUMN_LOG_TS = "log_ts";
    private static final String ROWS = " 500";

    private static final String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NAME
            + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_LOG_DATA + " TEXT, "
            + COLUMN_LOG_TS + " DATETIME DEFAULT (STRFTIME('%Y-%m-%d %H:%M:%f', 'NOW', 'localtime'))"
            + ");";

    public static void onCreate (SQLiteDatabase db) {
        if (db == null) {
            return;
        }

        try {
            db.execSQL(DATABASE_CREATE);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "DebugLogTable: Exception occurred while onCreate: " + e);
        }
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (db == null) {
            return;
        }

        try {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);

            Log.i(TAG, "DebugLogTable onUpgrade called. Executing drop_table query to clear old logs.");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "DebugLogTable: Exception occurred while onUpgrade: " + e);
        }
    }

    public static void addLog(SQLiteDatabase db, String sLog) {
        if (db == null || TextUtils.isEmpty(sLog)) {
            return;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_LOG_DATA, sLog);

        try {
            db.insert(TABLE_NAME, null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "DebugLogTable: Exception occurred while addLog: " + e);
        }
    }

    public static List<DbgLog> listLogs(SQLiteDatabase db) {
        List<DbgLog> dbgLogs = new ArrayList<>();
        if (db == null) {
            return dbgLogs;
        }

        Cursor c = db.rawQuery(
                "SELECT " + COLUMN_LOG_DATA + ", " +  COLUMN_LOG_TS + " FROM " + TABLE_NAME + " ORDER BY " + COLUMN_ID + " LIMIT  "+ ROWS,null);
        while (c.moveToNext()) {
            DbgLog dbgLog = new DbgLog();
            dbgLog.setLog_data(c.getString(c.getColumnIndex(COLUMN_LOG_DATA)));
            dbgLog.setLog_ts(c.getString(c.getColumnIndex(COLUMN_LOG_TS)));
            dbgLogs.add(dbgLog);
        }
        c.close();

        return dbgLogs;
    }

    public static void deleteAllLogs(SQLiteDatabase db) {
        if (db == null) {
            return;
        }

        try {
            db.delete(TABLE_NAME, null, null);
            db.delete("sqlite_sequence","name = '" + TABLE_NAME +"'",null);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "DebugLogTable: Exception occurred while deleteAllLogs: " + e);
        }
    }

    public static void clearOldLogs(SQLiteDatabase db) {
        if (db == null) {
            return;
        }

        try {
            db.delete(TABLE_NAME, COLUMN_LOG_TS + " <= datetime('now', 'localtime', '-7 day')", null);
            db.delete("sqlite_sequence","name = '" + TABLE_NAME +"'",null);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "DebugLogTable: Exception occurred while clearOldLogs: " + e);
        }
    }

    public static void clearLogs(SQLiteDatabase db, String sLogTs) {
        if (db == null && sLogTs.length() != 23) {
            return;
        }

        try {
            db.delete(TABLE_NAME, COLUMN_LOG_TS + " <= ?", new String[]{sLogTs});
            db.delete("sqlite_sequence","name = '" + TABLE_NAME +"'",null);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "DebugLogTable: Exception occurred while clearOldLogs: " + e);
        }
    }
}
