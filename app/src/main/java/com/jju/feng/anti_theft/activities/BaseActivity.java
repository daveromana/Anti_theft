package com.jju.feng.anti_theft.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;

import com.jju.feng.anti_theft.app.MyApplication;

/**
 * Created by Administrator on 2016/12/5 0005.
 */

public class BaseActivity extends Activity {
    public MyApplication application;
    private BaseActivity context;
    public SharedPreferences sp;
    public SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        editor = sp.edit();
        if (application == null) {
            application = (MyApplication) getApplication();
        }
        context = this;
        addActivity();
    }

    public void addActivity() {
        application.addActivity(context);
    }

    public void removeAllActivity() {
        application.removeAllActivity();
    }

}
