package com.dream.app.quicklaunch.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import com.dream.app.quicklaunch.R;

/**
 * Created by liushulong on 2014/11/25.
 */
public class QuickLaunchPickerItemView extends RelativeLayout {

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
    }
}
