package com.jju.feng.anti_theft.activities;

import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jju.feng.anti_theft.R;
import com.jju.feng.anti_theft.receiver.MyAdmin;
import com.jju.feng.anti_theft.ui.SettingCheckView;
import com.jju.feng.anti_theft.ui.SettingItemView;
import com.jju.feng.anti_theft.utils.IntentUtils;
import com.jju.feng.anti_theft.utils.Md5Utils;
import com.jju.feng.anti_theft.utils.ToastUtils;

public class Setup4Activity extends SetupBaseActivity {
    private SettingItemView siv_setup4_status;
    private TextView tv_setup4_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
        siv_setup4_status = (SettingItemView) findViewById(R.id.siv_setup4_status);
        tv_setup4_status = (TextView) findViewById(R.id.tv_setup4_status);
        String lockpassword = sp.getString("lockpassword", "1234");
        if (lockpassword.equals("1234")) {
            tv_setup4_status.setText("未设置");
        } else {
            tv_setup4_status.setText("已设置");
        }
        siv_setup4_status.setChecked(sp.getBoolean("protecting", false));
        siv_setup4_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (siv_setup4_status.isChecked()) {
                    siv_setup4_status.setChecked(false);
                    editor.putBoolean("protecting", false);
                } else {
                    siv_setup4_status.setChecked(true);
                    editor.putBoolean("protecting", true);
                }
                editor.commit();
            }
        });
    }

    /**
     * 点击设置防盗锁屏密码
     *
     * @param view
     */
    public void linear_setup4_psd(View view) {
        showSetupPasswordDialog();
    }


    /**
     * 设置密码对话框
     */

    private AlertDialog.Builder dialog;
    private Button btn_ok, btn_cancel;
    private EditText et_password;
    private AlertDialog alertDialog;
    private View view;

    private void showSetupPasswordDialog() {
        dialog = new AlertDialog.Builder(this);
        dialog.setCancelable(false);
        view = View.inflate(Setup4Activity.this, R.layout.dialog_setup_psd, null);
        et_password = (EditText) view.findViewById(R.id.et_enter_psd);
        final EditText et_password_confirm = (EditText) view.findViewById(R.id.et_password_confirm);
        btn_ok = (Button) view.findViewById(R.id.btn_ok);
        btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = et_password.getText().toString().trim();
                String password_confirm = et_password_confirm.getText().toString().trim();
                if (TextUtils.isEmpty(password) || TextUtils.isEmpty(password_confirm)) {
                    ToastUtils.showToast(Setup4Activity.this, "密码不能为空");
                } else if (!password.equals(password_confirm)) {
                    ToastUtils.showToast(Setup4Activity.this, "密码不一致");
                } else {
                    if (password.length() >= 4) {
                        editor.putString("lockpassword", password);
                        editor.commit();
                        alertDialog.dismiss();
                    } else {
                        ToastUtils.showToast(Setup4Activity.this, "密码不能少于4位");
                    }
                }
            }
        });
        dialog.setView(view);
        alertDialog = dialog.show();
    }

    /**
     * 点击激活设备超级管理员
     *
     * @param view
     */
    public void activeAdmin(View view) {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        ComponentName who = new ComponentName(this, MyAdmin.class);
        // 把要激活的组件名告诉系统
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, who);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                "开启后可以实现远程锁屏和销毁数据");
        startActivity(intent);
    }

    @Override
    public void showNext() {
        ToastUtils.showToast(this, "设置完成，修改配置文件");
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("finishSetup", true);
        editor.commit();
        //进入手机防盗的ui界面
        IntentUtils.startActivityAndFinish(this, LostFindActivity.class);

    }


    @Override
    public void showPre() {
        IntentUtils.startActivityAndFinish(this, Setup3Activity.class);

    }
}
