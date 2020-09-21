package com.crmretail_es.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class BootCompletedIntentReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(final Context context, Intent intent)
    {

        if("LogOutAction".equals(intent.getAction())){

            Log.e("LogOutAuto", intent.getAction());
            Toast.makeText(context, "Logout Action", Toast.LENGTH_SHORT).show();
            //Do your action
        }
    }
}