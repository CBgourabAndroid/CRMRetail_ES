package com.crmretail_es.shared;


import android.content.Context;
import android.content.SharedPreferences;

import com.crmretail_es.R;


public class UserShared {
    private final Context context;
    private final SharedPreferences prefs;

    public UserShared(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences("MY_SHARED_PREF", context.MODE_PRIVATE);
    }



    public String getId() {
        return prefs.getString(context.getString(R.string.shared_user_id), context.getString(R.string.shared_no_data));
    }


    public String getEmail() {
        return prefs.getString(context.getString(R.string.shared_user_email), context.getString(R.string.shared_no_data));
    }

    public String getphone() {
        return prefs.getString(context.getString(R.string.shared_userphone), context.getString(R.string.shared_no_data));
    }

    public String getFullName() {
        return prefs.getString(context.getString(R.string.shared_user_full_name), context.getString(R.string.shared_no_data));
    }

    public String getimage() {
        return prefs.getString(context.getString(R.string.shared_user_image), context.getString(R.string.shared_no_data));
    }




    public boolean getLoggedInStatus() {
        return prefs.getBoolean(context.getString(R.string.shared_loggedin_status), false);

    }

    public String getPersonalList() {
        return prefs.getString(context.getString(R.string.personallist), context.getString(R.string.shared_no_data));
    }

    public String getCheckList() {
        return prefs.getString(context.getString(R.string.checklist), context.getString(R.string.shared_no_data));
    }

    public String getAreaList() {
        return prefs.getString(context.getString(R.string.arealist), context.getString(R.string.shared_no_data));
    }
    public String getCustomerList() {
        return prefs.getString(context.getString(R.string.customerlist), context.getString(R.string.shared_no_data));
    }

    public String getProductList() {
        return prefs.getString(context.getString(R.string.productlist), context.getString(R.string.shared_no_data));
    }

    public String getVisitShop() {
        return prefs.getString(context.getString(R.string.shared_visit_status), "gb");
    }

    public String getEventSts() {
        return prefs.getString(context.getString(R.string.shared_event_status), context.getString(R.string.shared_no_data));
    }

    public String getEventID() {
        return prefs.getString(context.getString(R.string.shared_event_id), context.getString(R.string.shared_no_data));
    }


    public String getAccessToken() {
        return prefs.getString(context.getString(R.string.shared_access_token), context.getString(R.string.shared_no_data));
    }

    public boolean getDutyStatus() {
        return prefs.getBoolean(context.getString(R.string.shared_duty_status), false);

    }

    public String getDutyType() {
        return prefs.getString(context.getString(R.string.shared_duty_type), context.getString(R.string.shared_no_data));
    }

    public String getAreaData() {
        return prefs.getString(context.getString(R.string.areaData), context.getString(R.string.shared_no_data));
    }

    public String getCustomerinfo() {
        return prefs.getString(context.getString(R.string.customerinfo), context.getString(R.string.shared_no_data));
    }

    public String getUserPic() {
        return prefs.getString("userImg", context.getString(R.string.shared_no_data));
    }

    public String getNewPic() {
        return prefs.getString("newImg", context.getString(R.string.shared_no_data));
    }

    public String getCustomerinfo81() {
        return prefs.getString(context.getString(R.string.customerinfo81), context.getString(R.string.shared_no_data));
    }




}
