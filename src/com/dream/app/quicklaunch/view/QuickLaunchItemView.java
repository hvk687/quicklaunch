package com.dream.app.quicklaunch.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dream.app.quicklaunch.R;
import com.dream.app.quicklaunch.model.AppInfo;

/**
 * Created by liushulong on 2014/11/25.
 */
public class QuickLaunchItemView extends LinearLayout {
    ImageView icon;
    TextView name;

    public QuickLaunchItemView(Context context) {
        super(context);
        init();
    }

    public QuickLaunchItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public QuickLaunchItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setOrientation(HORIZONTAL);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        //setBackground(getResources().getDrawable(R.drawable.layout_bg_shadow));
        LayoutInflater.from(getContext()).inflate(R.layout.list_item_handle_left, this, true);
        icon = (ImageView) findViewById(R.id.icon);
        name = (TextView) findViewById(R.id.name);
    }

    public void bindView(AppInfo appInfo) {
        if (appInfo != null) {
            icon.setImageDrawable(appInfo.getAppIcon());
            name.setText(appInfo.getAppLabel());
        }
    }
}
