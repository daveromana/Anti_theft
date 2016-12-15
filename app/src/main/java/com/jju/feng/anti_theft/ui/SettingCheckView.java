package com.jju.feng.anti_theft.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jju.feng.anti_theft.R;

/**
 * Created by Administrator on 2016/12/8 0008.
 */

public class SettingCheckView extends LinearLayout {
    private TextView tv_title, tv_desc;
    private CheckBox cb_status;

    public SettingCheckView(Context context) {
        super(context);
        initView(context);
    }


    public SettingCheckView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        String bigtitle = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "bigtitle");
        tv_title.setText(bigtitle);

    }

    private void initView(Context context) {
        this.setOrientation(LinearLayout.HORIZONTAL);
        this.addView(View.inflate(context, R.layout.ui_setting_view, null));
        cb_status = (CheckBox) findViewById(R.id.cb_state);
        tv_title = (TextView) findViewById(R.id.tv_title);

    }


    /**
     * 判断组合控件是否被勾选
     */
    public boolean isChecked() {
        return cb_status.isChecked();
    }

    public void setChecked(boolean checked) {
        cb_status.setChecked(checked);
    }
}
