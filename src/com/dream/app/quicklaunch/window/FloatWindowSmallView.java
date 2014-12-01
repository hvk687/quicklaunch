package com.dream.app.quicklaunch.window;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import com.dream.app.quicklaunch.R;
import com.dream.app.quicklaunch.model.AppInfo;
import com.dream.app.quicklaunch.util.Utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class FloatWindowSmallView extends LinearLayout {
    private static final String TAG = FloatWindowSmallView.class.getSimpleName();
    /**
     * 记录小悬浮窗的宽度
     */
    public static int viewWidth;

    /**
     * 记录小悬浮窗的高度
     */
    public static int viewHeight;

    /**
     * 记录系统状态栏的高度
     */
    private static int statusBarHeight;

    /**
     * 用于更新小悬浮窗的位置
     */
    private WindowManager windowManager;

    /**
     * 小悬浮窗的参数
     */
    private WindowManager.LayoutParams mParams;

    /**
     * 记录当前手指位置在屏幕上的横坐标值
     */
    private float xInScreen;

    /**
     * 记录当前手指位置在屏幕上的纵坐标值
     */
    private float yInScreen;

    /**
     * 记录手指按下时在小悬浮窗的View上的横坐标的值
     */
    private float xInView;

    /**
     * 记录手指按下时在小悬浮窗的View上的纵坐标的值
     */
    private float yInView;

    List<AppInfo> mSelectedAppInfos = new ArrayList<AppInfo>();

    private int count = 0;
    private int TIME_DIFF = 300;
    private int TIME_DURATION = 2 * 1000;

    /**
     * start activity with given click times
     *
     * @param count
     */
    private void startActivity(int count) {
        if (mSelectedAppInfos == null || mSelectedAppInfos.size() <= 0) {
            mSelectedAppInfos = Utils.loadSelectedApp(getContext());
        }
        if (count > mSelectedAppInfos.size()) {
            //Toast to indicate user
        } else {
            AppInfo appInfo = mSelectedAppInfos.get(count);
            Intent intent = appInfo.getIntent();
            getContext().startActivity(intent);
        }
    }

    private boolean flag = false;

    public FloatWindowSmallView(Context context) {
        super(context);
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.float_window_small, this);
        View view = findViewById(R.id.small_window_layout);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
    }

    List<Long> mClickArray = new ArrayList<Long>();

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 手指按下时记录必要数据,纵坐标的值都需要减去状态栏高度
                xInView = event.getX();
                yInView = event.getY();
                xInScreen = event.getRawX();
                yInScreen = event.getRawY() - getStatusBarHeight();
                Log.d(TAG, "action down");

                long time = System.currentTimeMillis();
                int size = mClickArray.size() - 1;
                if (mClickArray.size() > 1) {
                    if (time - mClickArray.get(size - 1) > TIME_DURATION) {
                        mClickArray.clear();
                    }
                }

                mClickArray.add(time);
                flag = true;
                if (mClickArray.size() == 1) {
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (flag) {
                                if (mClickArray.size() <= 4) {
                                    handleUp(mClickArray.size());
                                    mClickArray.clear();
                                }
                            }
                        }
                    }, TIME_DURATION);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                xInScreen = event.getRawX();
                yInScreen = event.getRawY() - getStatusBarHeight();
                // 手指移动的时候更新小悬浮窗的位置
                updateViewPosition();
                float xdiff = (xInScreen - xInView);
                float ydiff = (yInScreen - yInView);
                Log.d(TAG, "action move");
                if (xdiff * xdiff + ydiff * ydiff < 25f) {

                } else {
                    flag = false;
                }

                break;
            case MotionEvent.ACTION_CANCEL:
                Log.d(TAG, "action cancel");
                flag = false;
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "action up");

                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 将小悬浮窗的参数传入，用于更新小悬浮窗的位置。
     *
     * @param params 小悬浮窗的参数
     */
    public void setParams(WindowManager.LayoutParams params) {
        mParams = params;
    }

    /**
     * 更新小悬浮窗在屏幕中的位置。
     */
    private void updateViewPosition() {
        mParams.x = (int) (xInScreen - xInView);
        mParams.y = (int) (yInScreen - yInView);
        windowManager.updateViewLayout(this, mParams);
    }

    /**
     * 用于获取状态栏的高度。
     *
     * @return 返回状态栏高度的像素值。
     */
    private int getStatusBarHeight() {
        if (statusBarHeight == 0) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object o = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = (Integer) field.get(o);
                statusBarHeight = getResources().getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusBarHeight;
    }

    private void handleUp(int count) {
        Log.d(TAG, "handleUp called!");
        if (mSelectedAppInfos == null || mSelectedAppInfos.size() <= 0) {
            mSelectedAppInfos = Utils.loadSelectedApp(getContext());
        }
        //start activity
        if (count <= 0 || count > mSelectedAppInfos.size()) {
            return;
        }

        getContext().startActivity(mSelectedAppInfos.get(count - 1).getIntent());
    }

    private Handler mHandler = new Handler();

}
