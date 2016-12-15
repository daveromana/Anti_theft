package com.jju.feng.anti_theft.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.jju.feng.anti_theft.utils.ToastUtils;

public class LockReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("TAG", "onReceive" + "哈哈哈，解锁了");
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        boolean protecting = sp.getBoolean("protecting", false);
        if (protecting) {
            if (intent != null
                    && Intent.ACTION_USER_PRESENT.equals(intent.getAction())) {
                ToastUtils.showToast(context, "已解锁", 0);
            }
        } else {

        }
    }
}
