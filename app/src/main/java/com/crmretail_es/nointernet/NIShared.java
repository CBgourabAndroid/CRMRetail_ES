package com.crmretail_es.nointernet;

import android.content.Context;
import android.content.SharedPreferences;

public class NIShared {
    private final Context context;
    private final SharedPreferences prefs;

    public NIShared(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences("MY_SHARED_PREF_GB", context.MODE_PRIVATE);
    }


    public String getLAList() {
        return prefs.getString("LeaveApplication", "");
    }


}
