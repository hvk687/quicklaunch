package com.dream.app.quicklaunch;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Toast;
import com.dream.app.quicklaunch.adapter.QuickLaunchAdapter;
import com.dream.app.quicklaunch.adapter.QuickLaunchDialogAdapter;
import com.dream.app.quicklaunch.lib.quicksort.DragSortController;
import com.dream.app.quicklaunch.lib.quicksort.DragSortListView;
import com.dream.app.quicklaunch.model.AppInfo;
import com.dream.app.quicklaunch.util.Utils;
import com.dream.app.quicklaunch.window.FloatWindowService;
import com.gc.materialdesign.views.ButtonFloat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    QuickLaunchAdapter mAdapter;
    List<AppInfo> mInstalledAppInfo = new ArrayList<AppInfo>();
    List<AppInfo> mSelectedAppInfos = new ArrayList<AppInfo>();

    ButtonFloat mBtnAdd;
    boolean isAdd = false; //true, add, false change;

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
        mInstalledAppInfo = Utils.queryAppInfo(this);
        /**
         * load selected applications
         *
         */
        mSelectedAppInfos = Utils.loadSelectedApp(this);
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
                Intent intent = new Intent(MainActivity.this, FloatWindowService.class);
                intent.setAction(FloatWindowService.ACTION_SHOW_WINDOW);
                startService(intent);
                finish();
            }
        });
        //btn
        mBtnAdd = (ButtonFloat) findViewById(R.id.add);
        mBtnAdd.setOnClickListener(mAddClickListener);
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
        Utils.saveSelectedApp(mSelectedAppInfos, this);
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
