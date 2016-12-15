package com.jju.feng.anti_theft.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.jju.feng.anti_theft.R;
import com.jju.feng.anti_theft.service.CallSmsSafeService;

import com.jju.feng.anti_theft.ui.SettingItemView;

public class SettingActivity extends BaseActivity {
    private SettingItemView siv_callsms_safe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


        //黑名单拦截设置
        siv_callsms_safe = (SettingItemView) findViewById(R.id.siv_callsms_safe);
        siv_callsms_safe.setChecked(sp.getBoolean("blacknumber", false));
        siv_callsms_safe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (siv_callsms_safe.isChecked()) {
                    siv_callsms_safe.setChecked(false);
                    editor.putBoolean("blacknumber", false);
                    stopService(new Intent(getApplicationContext(), CallSmsSafeService.class));
                } else {
                    siv_callsms_safe.setChecked(true);
                    editor.putBoolean("blacknumber", true);
                    startService(new Intent(getApplicationContext(), CallSmsSafeService.class));
                }
                editor.commit();

            }
        });
    }
}
