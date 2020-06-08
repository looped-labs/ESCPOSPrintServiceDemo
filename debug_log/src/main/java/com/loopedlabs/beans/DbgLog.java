package com.loopedlabs.beans;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.loopedlabs.util.debug.DebugLog;

public class DbgLog {
    @SerializedName("log_data")
    private String log_data;

    @SerializedName("log_ts")
    private String log_ts;

    public String getLog_data() {
        return log_data;
    }

    public void setLog_data(String log_data) {
        this.log_data = log_data;
    }

    public String getLog_ts() {
        return log_ts;
    }

    public void setLog_ts(String log_ts) {
        this.log_ts = log_ts;
    }

    public String toString() {
        return new Gson().toJson(this);
    }

    public void fromString(String sJson) {
        DebugLog.logTrace("log " + sJson);
        Gson g = new Gson();
        DbgLog a = g.fromJson(sJson,DbgLog.class);
        this.log_data = a.log_data;
        this.log_ts = a.log_ts;
    }
}
