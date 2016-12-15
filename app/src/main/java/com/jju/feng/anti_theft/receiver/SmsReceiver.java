package com.jju.feng.anti_theft.receiver;


import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.util.Log;

import com.jju.feng.anti_theft.R;
import com.jju.feng.anti_theft.service.GPSService;
import com.jju.feng.anti_theft.utils.Md5Utils;

import static android.content.Context.MODE_PRIVATE;


public class SmsReceiver extends BroadcastReceiver {
    private static String TAG = "SmsReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Object[] objs = (Object[]) intent.getExtras().get("pdus");
        DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName who = new ComponentName(context, MyAdmin.class);
        SharedPreferences sp = context.getSharedPreferences("config", MODE_PRIVATE);
        String lockpassword = sp.getString("lockpassword", "1234");
        String safenumber = sp.getString("safenumber", null);
        String safepsd = sp.getString("password", null);
        int psdlength = sp.getInt("passwordlength", 0);
        for (Object obj : objs) {
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
            String body = smsMessage.getMessageBody();
            String a = body.substring(0, body.length() - psdlength);
            String b = body.substring(body.length() - psdlength, body.length());
            String sender = smsMessage.getOriginatingAddress();
            if (sender.equals(safenumber)) {
                if ("#*location*#".equals(body)) {
                    Log.i(TAG, "返回手机的位置信息");
                    //获取用户的位置信息.
                    //后台获取.
                    Intent service = new Intent(context, GPSService.class);
                    context.startService(service);
                    abortBroadcast();
                } else if ("#*alarm*#".equals(body)) {
                    Log.i(TAG, "播放报警音乐");
                    MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
                    player.setVolume(1.0f, 1.0f);
                    player.start();
                    abortBroadcast();
                } else if ("#*wipedata*#".equals(body)) {
                    Log.i(TAG, "远程销毁数据");
                    if (dpm.isAdminActive(who)) {
                        dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
                    }
                    abortBroadcast();
                } else if ("#*lockscreen*#".equals(body)) {
                    Log.i(TAG, "远程锁屏");
                    if (dpm.isAdminActive(who)) {
                        dpm.resetPassword(lockpassword, 0);
                        dpm.lockNow();
                    }
                    abortBroadcast();
                }
            } else {
                //截取短信内容的后一位和前面的内容
                //String b = body.substring(body.length() - 1, body.length());
                //String a = body.substring(0, body.length() - 2);
                if (("#*location*#" + safepsd).equals(a + Md5Utils.encode(b))) {
                    Log.i(TAG, "返回手机的位置信息");
                    //获取用户的位置信息.
                    //后台获取.
                    Intent service = new Intent(context, GPSService.class);
                    context.startService(service);
                    abortBroadcast();
                } else if (("#*alarm*#" + safepsd).equals(a + Md5Utils.encode(b))) {
                    Log.i(TAG, "播放报警音乐");
                    MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
                    player.setVolume(1.0f, 1.0f);
                    player.start();
                    abortBroadcast();
                } else if (("#*wipedata*#" + safepsd).equals(a + Md5Utils.encode(b))) {
                    Log.i(TAG, "远程销毁数据");
                    if (dpm.isAdminActive(who)) {
                        dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
                    }
                    abortBroadcast();
                } else if (("#*lockscreen*#" + safepsd).equals(a + Md5Utils.encode(b))) {
                    Log.i(TAG, "远程锁屏");
                    if (dpm.isAdminActive(who)) {
                        dpm.resetPassword(lockpassword, 0);
                        dpm.lockNow();
                    }
                    abortBroadcast();
                }
            }
        }
    }
}