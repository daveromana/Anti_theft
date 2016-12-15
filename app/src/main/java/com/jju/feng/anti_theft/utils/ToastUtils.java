package com.jju.feng.anti_theft.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/12/7 0007.
 */

public class ToastUtils {
    /**
     * 显示吐司的工具类，安全的工具类可以在任意线程里面显示。
     *
     * @param activity
     * @param text
     */
    public static void showToast(final Activity activity, final String text) {
        if ("main".equalsIgnoreCase(Thread.currentThread().getName())) {
            Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
        } else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    /**
     * 显示吐司的工具类，安全的工具类可以在任意线程里面显示。
     *
     * @param activity
     * @param text
     * @param length   显示的时长
     */
    public static void showToast(final Context activity, final String text, final int length) {
        if ("main".equalsIgnoreCase(Thread.currentThread().getName())) {
            Toast.makeText(activity, text, length).show();
        } else {
            ((Activity) activity).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, text, length).show();

                }
            });
        }
    }
}
