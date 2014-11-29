package com.dream.app.quicklaunch.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.dream.app.quicklaunch.model.AppInfo;
import com.dream.app.quicklaunch.view.QuickLaunchPickerItemView;

/**
 * Created by liushulong on 2014/11/25.
 */
public class QuickLaunchDialogAdapter extends AbsListAdapter<AppInfo> {
    public QuickLaunchDialogAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int pos, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = new QuickLaunchPickerItemView(mContext);
        }
        QuickLaunchPickerItemView itemView = (QuickLaunchPickerItemView) view;
        itemView.bindView(getItem(pos));
        return view;
    }
}
