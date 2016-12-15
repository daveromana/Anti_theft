package com.jju.feng.anti_theft.app;

import android.app.Activity;
import android.app.Application;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/11 0011.
 */

public class MyApplication extends Application {

    private List<Activity> oActivity;

    @Override
    public void onCreate() {
        super.onCreate();

        oActivity = new ArrayList<Activity>();
    }


    public void addActivity(Activity activity) {
        if (!oActivity.contains(activity)) {
            oActivity.add(activity);
        }
    }

    public void removeActivity(Activity activity) {
        if (oActivity.contains(activity)) {
            oActivity.remove(activity);
            activity.finish();
        }
    }

    public void removeAllActivity() {
        for (Activity activity : oActivity) {
            activity.finish();
        }
    }
}
