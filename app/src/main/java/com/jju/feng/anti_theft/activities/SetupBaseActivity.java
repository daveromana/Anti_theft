package com.jju.feng.anti_theft.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.jju.feng.anti_theft.R;
import com.jju.feng.anti_theft.utils.ToastUtils;

/**
 * Created by Administrator on 2016/12/8 0008.
 */

public abstract class SetupBaseActivity extends BaseActivity {
    public SharedPreferences sp;
    //1.声明一个手势识别器
    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        //2.初始化一个手势识别器
        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {

            //onfling 滑行，滑翔
            //e1，e2 手指的事件，velocityX,velocityY 水平和竖直方向的速度
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (Math.abs(e1.getRawY() - e2.getRawY()) > 100) {
                    ToastUtils.showToast(SetupBaseActivity.this, "动作不合法");
                    return true;
                }
                if (e1.getRawX() - e2.getRawX() > 150) {
                    showNext();
                    overridePendingTransition(R.anim.trans_next_in, R.anim.trans_next_out);
                    return true;
                }
                if (e2.getRawX() - e1.getRawX() > 150) {
                    showPre();
                    overridePendingTransition(R.anim.trans_pre_in, R.anim.trans_pre_out);
                    return true;
                }


                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    //3.使用手势识别器
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public abstract void showNext();

    public void btn_next(View view) {
        showNext();
        overridePendingTransition(R.anim.trans_next_in, R.anim.trans_next_out);
    }

    public abstract void showPre();

    public void btn_pre(View view) {
        showPre();
        overridePendingTransition(R.anim.trans_pre_in, R.anim.trans_pre_out);
    }


}
