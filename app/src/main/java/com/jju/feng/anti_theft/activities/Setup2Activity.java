package com.jju.feng.anti_theft.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.jju.feng.anti_theft.R;
import com.jju.feng.anti_theft.ui.SettingCheckView;
import com.jju.feng.anti_theft.ui.SettingItemView;
import com.jju.feng.anti_theft.utils.IntentUtils;
import com.jju.feng.anti_theft.utils.ToastUtils;

public class Setup2Activity extends SetupBaseActivity {
    private SettingItemView siv_setup2_bind;
    private boolean isHasPermission = false;
    /**
     * 电话管理的服务，可以获取手机电话相关的信息
     */
    private TelephonyManager tm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
        //  getPermission();
        tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        siv_setup2_bind = (SettingItemView) findViewById(R.id.siv_setup2_bind);
        String sim = sp.getString("sim", null);
        if (TextUtils.isEmpty(sim)) {
            siv_setup2_bind.setChecked(false);
        } else {
            siv_setup2_bind.setChecked(true);
        }
        siv_setup2_bind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (siv_setup2_bind.isChecked()) {
                    //取消绑定，把保存的串号设置为空
                    siv_setup2_bind.setChecked(false);
                    editor.putString("sim", null);
                    editor.commit();
                } else {
                    //绑定，把串号记录下来
//                    if (!isHasPermission) {
//                        ToastUtils.showToast(Setup2Activity.this, "没有权限");
//                    } else {
//
//
//                    }

                    siv_setup2_bind.setChecked(true);
                    String sim = tm.getSimSerialNumber();
                    //tm.getLine1Number();  //得到手机卡的号码，sim卡的芯片上写着电话号码
                    editor.putString("sim", sim);
                    editor.commit();

                }
            }
        });
    }

    @Override
    public void showNext() {
        if (siv_setup2_bind.isChecked()) {
            IntentUtils.startActivityAndFinish(this, Setup3Activity.class);

        } else {
            ToastUtils.showToast(Setup2Activity.this, "要开启手机防盗，必须绑定sim卡串号");
        }

    }


    @Override
    public void showPre() {
        IntentUtils.startActivityAndFinish(this, Setup1Activity.class);

    }

//    public void getPermission() {
//        //权限申请
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
//                != PackageManager.PERMISSION_GRANTED) {
//            //做权限处理
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
//        } else {
//            isHasPermission = true;
//        }
//    }
//
//    /**
//     * 权限回调处理
//     *
//     * @param requestCode
//     * @param permissions
//     * @param grantResults
//     */
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        switch (requestCode) {
//            case 1:
//                //申请权限回调处理
//                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    isHasPermission = true;
//                } else {
//                    isHasPermission = false;
//                }
//                break;
//            default:
//                break;
//        }
//    }


}
