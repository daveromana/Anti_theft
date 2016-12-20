package com.jju.feng.anti_theft.activities;


import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.jju.feng.anti_theft.R;
import com.jju.feng.anti_theft.receiver.MyAdmin;
import com.jju.feng.anti_theft.service.BusAntiService;
import com.jju.feng.anti_theft.utils.ToastUtils;

public class BusAntiTheftActivity extends BaseActivity {
    private Intent service;
    private DevicePolicyManager dpm;
    private ComponentName who;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_anti_theft);
        dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        who = new ComponentName(BusAntiTheftActivity.this, MyAdmin.class);
        service = new Intent(BusAntiTheftActivity.this, BusAntiService.class);

    }


    // 开启公交防盗服务
    public void button(View view) {
        ToastUtils.showToast(this, "开启公交防盗！", 0);
        if (dpm.isAdminActive(who)) {
            dpm.lockNow();
        }
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    sleep(5000);
                    startService(service);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    // 关闭公交防盗服务
    public void close(View view) {

        stopService(service);


    }


}
