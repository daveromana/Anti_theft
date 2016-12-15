package com.jju.feng.anti_theft.activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.jju.feng.anti_theft.R;
import com.jju.feng.anti_theft.service.BusAntiService;
import com.jju.feng.anti_theft.utils.ToastUtils;

public class BusAntiTheftActivity extends BaseActivity {
    private Intent service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_anti_theft);
        service = new Intent(BusAntiTheftActivity.this, BusAntiService.class);

    }


    // 开启公交防盗服务
    public void button(View view) {
        ToastUtils.showToast(this, "开启公交防盗！", 0);
        startService(service);
    }

    // 关闭公交防盗服务
    public void close(View view) {

        stopService(service);


    }


}
