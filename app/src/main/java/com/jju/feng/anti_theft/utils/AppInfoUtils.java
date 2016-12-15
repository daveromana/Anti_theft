package com.jju.feng.anti_theft.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by Administrator on 2016/12/7 0007.
 */

public class AppInfoUtils {
    /**
     * 获取应用程序的版本名称
     *
     * @param context 上下文
     * @return App版本名称
     */
    public static String getAppVersionName(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo packinfo = pm.getPackageInfo(context.getPackageName(), 0);
            String versionName = packinfo.versionName;
            return versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }

    }

    /**
     * 获取应用程序的版本号
     *
     * @param context 上下文
     * @return App版本号
     */
    public static int getAppVersionCode(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo packinfo = pm.getPackageInfo(context.getPackageName(), 0);
            int versionCode = packinfo.versionCode;
            return versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }

    }
}
