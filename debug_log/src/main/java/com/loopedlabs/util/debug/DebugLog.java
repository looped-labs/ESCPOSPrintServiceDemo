package com.loopedlabs.util.debug;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import com.loopedlabs.db.DebugLogDb;
import com.loopedlabs.db.DebugLogDbHelper;
import com.loopedlabs.net.DebugLogRestClient;
import com.loopedlabs.net.NetUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

public class DebugLog {
    private static final String TAG = "DebugLog";
    private static boolean debugMode = false;
    private static SQLiteDatabase logDb = null;
    private static String sAppId = "";
    private static String sDeviceId = "";

    public static void initialize(Context context, String sAppId, String sDeviceId) {
        if (context == null) {
            Log.e(TAG, "DebugLog isn't initialized: Context couldn't be null");
            return;
        }
        DebugLog.sAppId = sAppId;
        DebugLog.sDeviceId = sDeviceId;

        Context applicationContext = context.getApplicationContext();

        synchronized (DebugLog.class) {
            if (logDb == null && applicationContext != null) {
                logDb = DebugLogDbHelper.getInstance(applicationContext);
            }
            DebugLogDb.clearOldLogs(logDb);
            if (NetUtils.isNetConnected(context)) {
               pushLogs();
            }
        }
    }

    public static void pushLogs() {
        if (logDb == null) {
            return;
        }
        DebugLogRestClient.pushLogs(DebugLog.sAppId, DebugLog.sDeviceId, DebugLogDb.listLogs(logDb));
    }

    public static void clearLogs(String sLogTs) {
        DebugLogDb.clearLogs(logDb,sLogTs);
    }

    public static void logTrace() {
        final StackTraceElement[] ste = Thread.currentThread()
                .getStackTrace();
        if (debugMode) {
            Log.d(ste[3].getClassName(), ste[3].getMethodName());
        }
        DebugLogDb.addLog(logDb, ste[3].getClassName()+ " --- " + ste[3].getMethodName());
    }

    public static void logTrace(String s) {
        final StackTraceElement[] ste = Thread.currentThread()
                .getStackTrace();
        if (debugMode) {
            Log.d(ste[3].getClassName(), ste[3].getMethodName() + " --- " + s);
        }
        DebugLogDb.addLog(logDb, ste[3].getClassName()+ " --- " + ste[3].getMethodName() + " --- " + s);
    }

    public static void logTrace(int s) {
        logTrace(String.valueOf(s));
    }

    public static void logTrace(double s) {
        logTrace(String.valueOf(s));
    }

    public static void logTrace(boolean s) {
        logTrace(String.valueOf(s));
    }

    public static void logTrace(Bundle b) {
        if (debugMode) {
            final StackTraceElement[] ste = Thread.currentThread()
                    .getStackTrace();
            JSONObject json = new JSONObject();
            Set<String> keys = b.keySet();
            for (String key : keys) {
                try {
                    json.put(key, JSONObject.wrap(b.get(key)));
                } catch(JSONException e) {
                    logException(e);
                }
            }
            Log.d(ste[3].getClassName(), ste[3].getMethodName() + " --- " + json.toString());
        }
    }

    public static void logException(Exception e) {
        final StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        Log.d(ste[3].getClassName(),
                ste[3].getMethodName() + " --- " + e.toString());
        Log.e(ste[3].getMethodName() + " --- ", e.toString(), e);
    }

    public static void logException(String msg, Exception e) {
        final StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        Log.d(ste[3].getClassName(),
                ste[3].getMethodName() + " --- " + e.toString());
        Log.e(ste[3].getMethodName() + " --- ", msg, e);
    }

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 3];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 3] = hexArray[v >>> 4];
            hexChars[j * 3 + 1] = hexArray[v & 0x0F];
            hexChars[j * 3 + 2] = ' ';
        }
        return new String(hexChars);
    }

    public static String bytesToHex(byte b) {
        char[] hexChars = new char[3];
        int v = b & 0xFF;
        hexChars[0] = hexArray[v >>> 4];
        hexChars[1] = hexArray[v & 0x0F];
        hexChars[2] = ' ';
        return new String(hexChars);
    }

    public static boolean isDebugMode() {
        return debugMode;
    }

    public static void setDebugMode(boolean debugMode) {
        DebugLog.debugMode = debugMode;
    }
}
