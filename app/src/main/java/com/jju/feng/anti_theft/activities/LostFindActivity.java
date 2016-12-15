package com.jju.feng.anti_theft.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jju.feng.anti_theft.R;
import com.jju.feng.anti_theft.utils.IntentUtils;
import com.jju.feng.anti_theft.utils.ToastUtils;

import java.util.List;

public class LostFindActivity extends BaseActivity {
    private TextView tv_lostfind_number, tv_lostfind_status;
    private ImageView iv_lostfind_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_find);
        tv_lostfind_number = (TextView) findViewById(R.id.tv_lostfind_number);
        iv_lostfind_status = (ImageView) findViewById(R.id.iv_lostfind_status);
        tv_lostfind_status = (TextView) findViewById(R.id.tv_lostfind_status);
        tv_lostfind_number.setText(sp.getString("safenumber", ""));
        boolean protecting = sp.getBoolean("protecting", false);
        if (protecting) {
            iv_lostfind_status.setImageResource(R.drawable.lock);
            tv_lostfind_status.setText("防盗保护已开启");
        } else {
            iv_lostfind_status.setImageResource(R.drawable.unlock);
            tv_lostfind_status.setText("防盗保护未开启");
        }


    }

    public void reEntrySetup(View view) {
        IntentUtils.startActivityAndFinish(this, Setup1Activity.class);
    }

    public void btn_back_command(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LostFindActivity.this);
        builder.setTitle("提示");
        builder.setCancelable(false);
        builder.setMessage("即将发送远程指令给安全号码！");
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String text = "GPS追踪:#*location*#,播放报警音乐: #*alarm*#,远程删除数据: #*wipedata*#,远程锁屏: #*lockscreen*#,非安全号码指令为：指令加手机防盗密码。例如：#*alarm*#000000";
                // #*location*#,播放报警音乐: #*alarm*#,远程删除数据: #*wipedata*#,远程锁屏: #*lockscreen*#,非安全号码指令为：指令加手机防盗密码。例如：#*alarm*#000000
                sendSms(text);
            }
        });
        builder.setPositiveButton("取消", null);
        builder.show();
    }

    private void sendSms(final String message) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                ToastUtils.showToast(LostFindActivity.this, "发送短信");
//                SmsManager.getDefault().sendTextMessage(sp.getString("safenumber", ""), null, "1235", null, null);
                //获取短信管理器
                String phonenumber = sp.getString("safenumber", "");
                android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
                //拆分短信内容（手机短信长度限制）
                List<String> divideContents = smsManager.divideMessage(message);
                for (String text : divideContents) {
                    smsManager.sendTextMessage(phonenumber, null, text, null, null);
                }

            }
        }.start();
    }


}
