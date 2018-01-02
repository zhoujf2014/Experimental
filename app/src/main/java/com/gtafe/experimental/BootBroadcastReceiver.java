
package com.gtafe.experimental;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.gtafe.experimental.activity.MainActivity;


/**
 * Created by ZhouJF on 2017/12/14.
 */

public class BootBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "BootBroadcastReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "onReceive: 收到开机广播");
        Intent intent1 = new Intent(context, MainActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);
      /*  Intent app = context.getPackageManager().getLaunchIntentForPackage("com.gtafe.agriculture");
        context.startActivity(app );*/
    }
}
