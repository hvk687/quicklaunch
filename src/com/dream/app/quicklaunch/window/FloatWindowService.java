package com.dream.app.quicklaunch.window;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

public class FloatWindowService extends Service {
    private static final String TAG = FloatWindowService.class.getSimpleName();
    public static final String ACTION_SHOW_WINDOW = "action_show_window";
    public static final String ACTION_HIDE_WINDOW = "action_hide_window";

    public static final String ACTION_REPEAT_SERVICE = "action_repeat_service";

    @Override
    public void onCreate() {
        super.onCreate();startRepeatingSystemService(this);

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {
            Log.d(TAG, "onStartCommand:" + intent.getAction());

            String action = intent.getAction();
            if (ACTION_SHOW_WINDOW.equals(action)) {
                showWindow();
            } else if (ACTION_HIDE_WINDOW.equals(action)) {
                hideWindow();
            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    private void showWindow() {
        if (!MyWindowManager.isWindowShowing()) {
            MyWindowManager.createSmallWindow(getApplicationContext());
        }
    }

    private void hideWindow() {
        MyWindowManager.removeSmallWindow(getApplicationContext());
    }

    public void startRepeatingSystemService(Context context) {


        Intent intent = new Intent(context, RepeatBroadcastReceiver.class);
        intent.setAction(FloatWindowService.ACTION_REPEAT_SERVICE);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);

        //开始时间
        long firstime = SystemClock.elapsedRealtime();
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(sender);//cancel if we have set one
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstime, 5 * 1000, sender);
    }
}
