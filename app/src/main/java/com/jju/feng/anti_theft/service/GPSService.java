package com.jju.feng.anti_theft.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.PowerManager;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.jju.feng.anti_theft.app.MyApplication;

import java.util.List;


public class GPSService extends Service {
    private PowerManager.WakeLock m_wklk;
    private LocationService locationService;
    private int count = 0; //控制发送短信的次数

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //申请WakeLock可以让进程持续执行即使手机进入睡眠模式
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        m_wklk = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, GPSService.class.getName());
        m_wklk.acquire();

        locationService = ((MyApplication) getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        //注册监听
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        locationService.start();// 定位SDK

    }

    /**
     * 发送短信的方法
     *
     * @param message 要发送的内容
     */
    private void sendSms(final String message) {
        new Thread() {
            @Override
            public void run() {
                super.run();
//                SmsManager.getDefault().sendTextMessage(sp.getString("safenumber", ""), null, "1235", null, null);
                //获取短信管理器
                SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
                String phonenumber = sp.getString("safenumber", "");
                android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
                //拆分短信内容（手机短信长度限制）
                List<String> divideContents = smsManager.divideMessage(message);
                for (String text : divideContents) {
                    smsManager.sendTextMessage(phonenumber, null, text, null, null);
                }
                //停止服务
                stopSelf();

            }
        }.start();
    }


    /*****
     *  copy funtion to you project
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                StringBuffer sb = new StringBuffer(256);
                sb.append("time : ");
                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
                sb.append(location.getTime());
                sb.append("\nlatitude : ");// 纬度
                sb.append(location.getLatitude());
                sb.append(";lontitude : ");// 经度
                sb.append(location.getLongitude());

                sb.append("\naddr : ");// 地址信息
                sb.append(location.getAddrStr());
                sb.append(location.getLocationDescribe());// 位置语义化信息
                count++;
                if (count == 1) {
                    sendSms(sb.toString());
                }
            }
        }

    };


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (m_wklk != null) {
            m_wklk.release();
            m_wklk = null;
        }
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
    }
}
