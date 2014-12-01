package com.dream.app.quicklaunch.window;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by liushulong on 2014/12/1.
 */
public class RepeatBroadcastReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
                Intent start = new Intent(context, FloatWindowService.class);
                start.setAction(FloatWindowService.ACTION_SHOW_WINDOW);
                context.startService(intent);
            } else if (FloatWindowService.ACTION_REPEAT_SERVICE.equals(intent.getAction())) {
                if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
                    Intent start = new Intent(context, FloatWindowService.class);
                    start.setAction(FloatWindowService.ACTION_SHOW_WINDOW);
                    context.startService(intent);
                }
            }
        }
    }
}
