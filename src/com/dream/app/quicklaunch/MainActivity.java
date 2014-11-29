package com.dream.app.quicklaunch;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.dream.app.quicklaunch.adapter.QuickLaunchAdapter;
import com.dream.app.quicklaunch.adapter.QuickLaunchDialogAdapter;
import com.dream.app.quicklaunch.lib.quicksort.DragSortController;
import com.dream.app.quicklaunch.lib.quicksort.DragSortListView;
import com.dream.app.quicklaunch.model.AppInfo;
import com.dream.app.quicklaunch.util.SharedPreferenceUtils;
import com.dream.app.quicklaunch.util.StringUtils;
import com.gc.materialdesign.views.ButtonFloat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends Activity {

    QuickLaunchAdapter mAdapter;
    List<AppInfo> mInstalledAppInfo = new ArrayList<AppInfo>();
    List<AppInfo> mSelectedAppInfos = new ArrayList<AppInfo>();
    List<AppInfo> mDefaultAppInfos = new ArrayList<AppInfo>();
    ButtonFloat mBtnAdd;
    boolean isAdd = false; //true, add, false change;
    private static final String SELECTED_APP_LIST = "selected_app_list";
    Dialog mPickerDialog;
    int mClickedPos = 0;
    QuickLaunchDialogAdapter mPickerAdapter;
    private DragSortListView.DropListener onDrop =
            new DragSortListView.DropListener() {
                @Override
                public void drop(int from, int to) {
                    if (from != to) {
                        AppInfo item = mSelectedAppInfos.get(from);
                        mSelectedAppInfos.remove(from);
                        mSelectedAppInfos.add(to, item);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            };

    private DragSortListView.RemoveListener onRemove =
            new DragSortListView.RemoveListener() {
                @Override
                public void remove(int which) {
                    mSelectedAppInfos.remove(which);
                    mAdapter.notifyDataSetChanged();
                }
            };

    private DragSortListView mDslv;
    private DragSortController mController;

    public int dragStartMode = DragSortController.ON_DOWN;
    public boolean removeEnabled = true;
    public int removeMode = DragSortController.FLING_REMOVE;
    public boolean sortEnabled = true;
    public boolean dragEnabled = true;

    /**
     * Called in onCreateView. Override this to provide a custom
     * DragSortController.
     */
    public DragSortController buildController(DragSortListView dslv) {
        DragSortController controller = new DragSortController(dslv);
        controller.setDragHandleId(R.id.drag_handle);
        controller.setClickRemoveId(R.id.click_remove);
        controller.setRemoveEnabled(removeEnabled);
        controller.setSortEnabled(sortEnabled);
        controller.setDragInitMode(dragStartMode);
        controller.setRemoveMode(removeMode);
        return controller;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        initData();
        initView();
        initDialog();
    }

    private void initData() {

        /**
         * query all installed applications into list
         */
        queryAppInfo();
        /**
         * load selected applications
         *
         */
        loadSelectedApp();
    }

    private void initView() {
        mDslv = (DragSortListView) findViewById(android.R.id.list);
        mController = buildController(mDslv);
        mDslv.setFloatViewManager(mController);
        mDslv.setOnTouchListener(mController);
        mDslv.setDragEnabled(dragEnabled);
        mDslv.setDropListener(onDrop);
        mDslv.setRemoveListener(onRemove);
        mDslv.setOnItemClickListener(mItemClickListener);
        mAdapter = new QuickLaunchAdapter(this);
        mAdapter.setDataList(mSelectedAppInfos);
        mDslv.setAdapter(mAdapter);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //btn
        mBtnAdd = (ButtonFloat) findViewById(R.id.add);
        mBtnAdd.setOnClickListener(mAddClickListener);
    }

    public void queryAppInfo() {
        mInstalledAppInfo.clear();
        PackageManager pm = this.getPackageManager(); // 获得PackageManager对象
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        // 通过查询，获得所有ResolveInfo对象.
        List<ResolveInfo> resolveInfos = pm
                .queryIntentActivities(mainIntent, 0);
        // 调用系统排序 ， 根据name排序
        // 该排序很重要，否则只能显示系统应用，而不能列出第三方应用程序
        Collections.sort(resolveInfos, new ResolveInfo.DisplayNameComparator(pm));
        if (mInstalledAppInfo == null) {
            mInstalledAppInfo = new ArrayList<AppInfo>();
        }
        mInstalledAppInfo.clear();
        //add home to the first
        mInstalledAppInfo.add(loadDefaultHome());
        //add other app info
        for (ResolveInfo reInfo : resolveInfos) {
            mInstalledAppInfo.add(makeAppInfo(reInfo, pm)); // 添加至列表中
        }
    }

    private void initDialog() {
        //init dialog adapter
        mPickerAdapter = new QuickLaunchDialogAdapter(this);
        mPickerDialog = new AlertDialog.Builder(this).
                setTitle(R.string.app_picker_title).
                setPositiveButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mPickerDialog.dismiss();
                    }
                }).
                setAdapter(mPickerAdapter, mDialogClickListener).create();
        mPickerAdapter.setDataList(mInstalledAppInfo);
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveSelectedApp();
    }


    /**
     *
     */
    private void saveSelectedApp() {
        try {
            String jsonString = JSON.toJSONString(mSelectedAppInfos);
            SharedPreferenceUtils.setSharedPreferences(SELECTED_APP_LIST, jsonString, this);
        } catch (Exception e) {
            SharedPreferenceUtils.setSharedPreferences(SELECTED_APP_LIST, "", this);
        }
    }

    /**
     *
     */
    private void loadSelectedApp() {
        mSelectedAppInfos.clear();
        try {
            String json = SharedPreferenceUtils.getSharedPreferences(SELECTED_APP_LIST, this);
            if (!StringUtils.isNullOrEmpty(json)) {
                mSelectedAppInfos = JSON.parseArray(json, AppInfo.class);
                parseDrawable();
                return;
            }
        } catch (Exception e) {
        }
        /**
         * if code run here, it means, we should load default
         * app info
         */
        AppInfo home = loadDefaultHome();
        if (home != null) {
            mSelectedAppInfos.add(home);
        }
        AppInfo dialer = loadDefaultPhoneApp();
        if (dialer != null) {
            mSelectedAppInfos.add(dialer);
        }
    }

    /**
     * after load saved app information,
     * we should set the drawable for them
     */
    private void parseDrawable() {
        if (mSelectedAppInfos == null || mSelectedAppInfos.size() <= 0) {
            return;
        }
        PackageManager pm = this.getPackageManager(); // 获得PackageManager对象

        for (AppInfo info : mSelectedAppInfos) {
            Intent mainIntent = new Intent();
            mainIntent.setPackage(info.getPkgName());
            List<ResolveInfo> resolveInfos = pm
                    .queryIntentActivities(mainIntent, 0);
            if (resolveInfos != null && resolveInfos.size() > 0) {
                AppInfo parsed = makeAppInfo(resolveInfos.get(0), pm);
                info.setIntent(parsed.getIntent());
                info.setAppIcon(parsed.getAppIcon());
            }
        }
    }

    /**
     * start home activity
     */
    private AppInfo loadDefaultHome() {
        PackageManager pm = this.getPackageManager(); // 获得PackageManager对象
        Intent mainIntent = new Intent(Intent.ACTION_MAIN);
        mainIntent.addCategory(Intent.CATEGORY_HOME);

        // 通过查询，获得所有ResolveInfo对象.
        List<ResolveInfo> resolveInfos = pm
                .queryIntentActivities(mainIntent, PackageManager.MATCH_DEFAULT_ONLY);

        if (resolveInfos != null && resolveInfos.size() > 0) {
            return makeAppInfo(resolveInfos.get(0), pm);
        }

        return null;
    }

    private AppInfo loadDefaultPhoneApp() {
        PackageManager pm = this.getPackageManager(); // 获得PackageManager对象

        Intent mainIntent = new Intent(Intent.ACTION_DIAL);
        // 通过查询，获得所有ResolveInfo对象.
        List<ResolveInfo> resolveInfos = pm
                .queryIntentActivities(mainIntent, PackageManager.MATCH_DEFAULT_ONLY);
        if (resolveInfos != null && resolveInfos.size() > 0) {
            return makeAppInfo(resolveInfos.get(0), pm); // 添加至列表中
        }

        return null;
    }

    private AppInfo makeAppInfo(ResolveInfo reInfo, PackageManager pm) {
        String activityName = reInfo.activityInfo.name; // 获得该应用程序的启动Activity的name
        String pkgName = reInfo.activityInfo.packageName; // 获得应用程序的包名
        String appLabel = (String) reInfo.loadLabel(pm); // 获得应用程序的Label
        Drawable icon = reInfo.loadIcon(pm); // 获得应用程序图标
        // 为应用程序的启动Activity 准备Intent
        Intent launchIntent = new Intent();
        launchIntent.setComponent(new ComponentName(pkgName, activityName));
        // 创建一个AppInfo对象，并赋值
        AppInfo appInfo = new AppInfo();
        appInfo.setAppLabel(appLabel);
        appInfo.setPkgName(pkgName);
        appInfo.setAppIcon(icon);
        appInfo.setIntent(launchIntent);
        return appInfo;
    }

    /**
     * user choose one item
     */
    private DialogInterface.OnClickListener mDialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            if (!isAdd) {
                mSelectedAppInfos.set(mClickedPos, mInstalledAppInfo.get(i));
                mAdapter.notifyDataSetChanged();
            } else {
                /**
                 * filter, DO NOT add the package if user has added this package
                 */
                for (AppInfo info : mSelectedAppInfos) {
                    if (info.getPkgName().equals(mInstalledAppInfo.get(i).getPkgName())) {
                        Toast.makeText(MainActivity.this, R.string.app_duplcate_hint, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                /**
                 * add the app to list if not added before
                 */
                mSelectedAppInfos.add(mInstalledAppInfo.get(i));
                mAdapter.notifyDataSetChanged();
            }
        }
    };

    /**
     * change click listener
     */
    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            mClickedPos = i;
            isAdd = false;
            mPickerDialog.show();
        }
    };
    /**
     * add click listener
     */
    private View.OnClickListener mAddClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mSelectedAppInfos != null && mSelectedAppInfos.size() < 4) {
                isAdd = true;
                mPickerDialog.show();
            } else {
                Toast.makeText(MainActivity.this, R.string.app_limit_hint, Toast.LENGTH_SHORT).show();
            }
        }
    };
}
