package com.dream.app.quicklaunch.window;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by liushulong on 2014/12/1.
 */
public class RepeatBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = RepeatBroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive:" + intent.getAction());
        if (intent != null) {
            if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
                Intent start = new Intent(context, FloatWindowService.class);
                start.setAction(FloatWindowService.ACTION_SHOW_WINDOW);
                context.startService(start);
            } else if (FloatWindowService.ACTION_REPEAT_SERVICE.equals(intent.getAction())) {
                Intent start = new Intent(context, FloatWindowService.class);
                start.setAction(FloatWindowService.ACTION_SHOW_WINDOW);
                context.startService(start);
            }
        }
    }
}
