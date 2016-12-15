package com.jju.feng.anti_theft.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("TAG", "onReceive" + "哈哈哈，手机启动完成了");
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        boolean protecting = sp.getBoolean("protecting", false);
        if (protecting) {
            Log.i("TAG", "onReceive" + "开启了防盗保护，检查sim卡的状态");
            //1.取出来当前手机的sim卡串号
            TelephonyManager tm = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
            String realsim = tm.getSimSerialNumber();
            //2.取出之前存的sim卡串号
            String bindsim = sp.getString("sim", "");
            //3.判断两个串号是否一致
            if (realsim.equals(bindsim)) {
                //sim卡没有变化，还是你的手机卡
            } else {
                //sim卡发生变化，手机卡能被盗，偷偷的在后台给安全号码发送一个报警短信
                SmsManager.getDefault().sendTextMessage(sp.getString("safenumber", ""), null, "sim changed", null, null);

            }
        } else {
            Log.i("TAG", "onReceive" + "没有开启防盗保护");
        }
    }
}
