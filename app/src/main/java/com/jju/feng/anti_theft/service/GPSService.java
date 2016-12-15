package com.jju.feng.anti_theft.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.telephony.SmsManager;


public class GPSService extends Service {
    private PowerManager.WakeLock m_wklk;

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

        final LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        //指定精确度
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        //指定耗电量
        criteria.setPowerRequirement(Criteria.POWER_HIGH);

        String provider = lm.getBestProvider(criteria, true);
        System.out.println("最好的提供者:" + provider);

        lm.requestLocationUpdates(provider, 0, 0, new LocationListener() {
            //位置发生变化时调用的方法
            @Override
            public void onLocationChanged(Location location) {
                location.getLongitude(); //经度
                location.getLatitude();  //纬度
                String text = ("J:" + location.getLongitude() + "W:" + location.getLatitude());
                SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
                SmsManager.getDefault().sendTextMessage(sp.getString("safenumber", ""), null, text, null, null);
                lm.removeUpdates(this);
                stopSelf();
            }

            //当位置提供者状态变化调用的方法.
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            //当位置提供者可用的时候调用的方法.
            @Override
            public void onProviderEnabled(String provider) {

            }

            //当位置提供者不可用的时候调用的方法.
            @Override
            public void onProviderDisabled(String provider) {

            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (m_wklk != null) {
            m_wklk.release();
            m_wklk = null;
        }
    }
}
