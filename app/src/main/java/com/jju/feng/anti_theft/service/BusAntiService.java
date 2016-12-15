package com.jju.feng.anti_theft.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import com.jju.feng.anti_theft.R;

public class BusAntiService extends Service {
    private PowerManager.WakeLock m_wklk;
    private SensorManager smManager;
    private Sensor sensor;
    private MyListener listener;
    private MediaPlayer player;

    public BusAntiService() {
    }

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

        player = MediaPlayer.create(this, R.raw.ylzs);
        //获取光传感器
        //获取SensorManager对象
        smManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        //获取Sensor对象
        sensor = smManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        listener = new MyListener();
        smManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL);

    }

    private class MyListener implements SensorEventListener {
        int count = 0;

        // 当传感器检测到数据变化
        @Override
        public void onSensorChanged(SensorEvent event) {
            //获取精度
            float acc = event.accuracy;
            //获取光线强度
            float light = event.values[0];
            Log.e("sssssssss", "光强" + light);
            if (light > 30) {
                count++;
                if (count == 1) {
                    player.setVolume(1.0f, 1.0f);
                    player.setLooping(true);
                    player.start();
                    //    handler.sendEmptyMessage(123);
                }

            }
        }

        // 当传感器精确度发生变化
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 123:
                    Toast.makeText(getApplicationContext(), "*****", 0).show();
                    break;
            }

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("destory", " :销毁服务 ");
        if (smManager != null) {
            smManager.unregisterListener(listener);
            if (player.isPlaying()) {
                player.stop();
                player.release();
            }
        }
        if (m_wklk != null) {
            m_wklk.release();
            m_wklk = null;
        }
    }
}
