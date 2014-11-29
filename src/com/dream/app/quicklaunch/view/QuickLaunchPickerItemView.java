package com.dream.app.quicklaunch.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dream.app.quicklaunch.R;
import com.dream.app.quicklaunch.model.AppInfo;

/**
 * Created by liushulong on 2014/11/25.
 */
public class QuickLaunchPickerItemView extends RelativeLayout {
    ImageView icon;
    TextView name;
    TextView packageName;

    public QuickLaunchPickerItemView(Context context) {
        super(context);
        init();
    }

    public QuickLaunchPickerItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public QuickLaunchPickerItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_picker_item, this);
        icon = (ImageView) findViewById(R.id.icon);
        name = (TextView) findViewById(R.id.name);
        packageName = (TextView) findViewById(R.id.package_name);
    }

    public void bindView(AppInfo appInfo) {
        icon.setImageDrawable(appInfo.getAppIcon());
        name.setText(appInfo.getAppLabel());
        packageName.setText(appInfo.getPkgName());
    }
}
