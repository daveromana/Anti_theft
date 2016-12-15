package com.jju.feng.anti_theft.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.jju.feng.anti_theft.R;

/**
 * Created by Administrator on 2016/12/7 0007.
 */

public class IntentUtils {
    /**
     * 开启一个新的activity
     *
     * @param context
     * @param cls
     */
    public static void startActivity(Activity context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);
    }

    /**
     * 开启一个新的activity并finish当前的
     *
     * @param context
     * @param cls
     */
    public static void startActivityAndFinish(Activity context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);
        context.finish();
    }


    /**
     * 延时开启一个activity
     *
     * @param context
     * @param cls
     * @param delaytime 延时执行的时间
     */
    public static void startActivityForDelay(final Activity context, final Class<?> cls, final long delaytime) {


        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(delaytime);
                    Intent intent = new Intent(context, cls);
                    context.startActivity(intent);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 延时开启一个新的activity并finish之前的activity
     *
     * @param context
     * @param cls
     * @param delaytime 延时执行的时间
     */
    public static void startActivityForDelayAndFinish(final Activity context, final Class<?> cls, final long delaytime) {


        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(delaytime);
                    Intent intent = new Intent(context, cls);
                    context.startActivity(intent);
                    context.finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
