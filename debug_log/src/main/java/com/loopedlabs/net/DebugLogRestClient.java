package com.loopedlabs.net;

import android.util.Log;

import com.google.gson.Gson;
import com.loopedlabs.beans.DbgLog;
import com.loopedlabs.util.debug.DebugLog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class DebugLogRestClient {
    private static final String BASE_URL = "https://api.mobills.in/rpos/";
    private static boolean bPushing = false;
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void pushLogs(String app_id, String device_id, List<DbgLog> dbgLogs) {
        if (dbgLogs.size() <= 0 || bPushing) {
            return;
        }
        bPushing = true;
        Log.d("DebugLogRestClient","Triggering Push Logs");
        client.post(getAbsoluteUrl("log-log"), makeParams(app_id, device_id, dbgLogs),  new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                bPushing = false;
                try {
                    Log.d("DebugLogRestClient",response.getString("last_sync_ts"));
                    DebugLog.clearLogs(response.getString("last_sync_ts"));
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                bPushing = false;
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("DebugLogRestClient","Network Error : " + responseString);
            }
        });
    }

    private static RequestParams makeParams(String app_id, String device_id, List<DbgLog> dbgLogs) {
        RequestParams params = new RequestParams();
        params.put("app_id", app_id);
        params.put("device_id", device_id);
        params.put("log", new Gson().toJson(dbgLogs));
        return params;
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
