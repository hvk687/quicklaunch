package com.dream.app.quicklaunch.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.dream.app.quicklaunch.model.AppInfo;
import com.dream.app.quicklaunch.view.QuickLaunchItemView;

/**
 * Created by liushulong on 2014/11/25.
 */
public class QuickLaunchAdapter extends AbsListAdapter<AppInfo> {
    public QuickLaunchAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int pos, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = new QuickLaunchItemView(mContext);
        }
        QuickLaunchItemView itemView = (QuickLaunchItemView) view;
        itemView.bindView(getItem(pos));
        return view;
    }
}
