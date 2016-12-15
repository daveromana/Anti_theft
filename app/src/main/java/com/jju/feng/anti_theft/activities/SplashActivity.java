package com.jju.feng.anti_theft.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jju.feng.anti_theft.R;
import com.jju.feng.anti_theft.utils.AppInfoUtils;
import com.jju.feng.anti_theft.utils.IntentUtils;

public class SplashActivity extends BaseActivity {

    private RelativeLayout lin;
    private LinearLayout but;
    private TextView time;
    private int count = 5;
    private TextView tv_splash_version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
        lin = (RelativeLayout) findViewById(R.id.lin);
        but = (LinearLayout) findViewById(R.id.but);
        time = (TextView) findViewById(R.id.time);

        tv_splash_version.setText(AppInfoUtils.getAppVersionName(SplashActivity.this));

        lin.setBackgroundResource(R.drawable.bg11);
        handler.sendEmptyMessage(0x123);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = 0;
                handler.sendEmptyMessage(0x123);
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x123) {
                if (count == 0) {
                    IntentUtils.startActivityAndFinish(SplashActivity.this, HomeActivity.class);
                    //IntentUtils.startActivityForDelayAndFinish(SplashActivity.this, HomeActivity.class, 0);
                    overridePendingTransition(R.anim.trans_next_in, R.anim.trans_next_out);
                    handler.removeMessages(0x123);
                    finish();
                } else {
                    count--;
                    time.setText(count + "");
                    handler.sendEmptyMessageDelayed(0x123, 1000);
                }
            }
        }
    };
}
