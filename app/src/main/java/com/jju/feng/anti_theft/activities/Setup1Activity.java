package com.jju.feng.anti_theft.activities;

import android.os.Bundle;
import android.view.View;

import com.jju.feng.anti_theft.R;
import com.jju.feng.anti_theft.utils.IntentUtils;

public class Setup1Activity extends SetupBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);
    }
    //进入下一个界面
    @Override
    public void showNext() {
        IntentUtils.startActivityAndFinish(Setup1Activity.this, Setup2Activity.class);
    }
    //返回上一个界面
    @Override
    public void showPre() {

    }
}
