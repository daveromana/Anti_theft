package com.jju.feng.anti_theft.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.jju.feng.anti_theft.R;
import com.jju.feng.anti_theft.adapter.GridViewAdapter;
import com.jju.feng.anti_theft.domain.Module;
import com.jju.feng.anti_theft.utils.IntentUtils;
import com.jju.feng.anti_theft.utils.Md5Utils;
import com.jju.feng.anti_theft.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends BaseActivity {
    private String[] module_names = new String[]{"手机防盗", "通讯卫士", "公交防盗", "进程管理",
            "定位测试", "设置中心"};
    private int[] module_imgs = new int[]{R.drawable.safe,
            R.drawable.callmsgsafe, R.drawable.app, R.drawable.taskmanager,
            R.drawable.netmanager, R.drawable.settings};
    private List<Module> olist = new ArrayList<Module>();
    private GridViewAdapter adapter;
    private GridView gridView;
    private AlertDialog alertDialog;
    private View view;
    private AlertDialog.Builder dialog;
    private Button btn_ok, btn_cancel;
    private EditText et_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init_view();
    }

    /**
     * gridview的监听事件
     */
    private AdapterView.OnItemClickListener onItemlistener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 0://手机防盗
                    //判断用户是否设置过手机防盗的密码
                    String password = sp.getString("password", null);
                    if (TextUtils.isEmpty(password)) {
                        //弹出设置密码的对话框
                        showSetupPasswordDialog();
                    } else {
                        //弹出输入密码的对话框
                        showEnterPasswordDialog();
                    }
                    break;
                case 1: //黑名单模块
                    IntentUtils.startActivity(HomeActivity.this, CallSmsSafeActivity.class);
                    break;
                case 2:
                    //公交防盗模块
                    IntentUtils.startActivity(HomeActivity.this, BusAntiTheftActivity.class);
                    break;
                case 3:
                    break;
                case 4:
                    //测试定位模块
                    IntentUtils.startActivity(HomeActivity.this, TestLocationActivity.class);
                    break;

                case 5:
                    //设置中心模块
                    IntentUtils.startActivity(HomeActivity.this, SettingActivity.class);
                    break;
            }
        }
    };


    /**
     * 输入密码对话框
     */
    private void showEnterPasswordDialog() {
        dialog = new AlertDialog.Builder(this);
        view = View.inflate(HomeActivity.this, R.layout.dialog_enter_psd, null);
        et_password = (EditText) view.findViewById(R.id.et_enter_psd);
        dialog.setView(view);
        dialog.setCancelable(false);
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
                String inputPassword = et_password.getText().toString().trim();
                String saved_password = sp.getString("password", null);
                if (TextUtils.isEmpty(inputPassword)) {
                    ToastUtils.showToast(HomeActivity.this, "密码不能为空");
                } else if (Md5Utils.encode(inputPassword).equals(saved_password)) {
                    ToastUtils.showToast(HomeActivity.this, "密码输入正确，进入手机防盗界面");
                    alertDialog.dismiss();
                    boolean finishSetup = sp.getBoolean("finishSetup", false);
                    if (finishSetup) {
                        //进入手机防盗界面
                        IntentUtils.startActivity(HomeActivity.this, LostFindActivity.class);
                    } else {
                        //否，则进入设置向导界面
                        IntentUtils.startActivity(HomeActivity.this, Setup1Activity.class);

                    }
                } else {
                    ToastUtils.showToast(HomeActivity.this, "密码输入错误");
                }
            }
        });
        alertDialog = dialog.show();

    }

    /**
     * 设置密码对话框
     */


    private void showSetupPasswordDialog() {
        dialog = new AlertDialog.Builder(this);
        dialog.setCancelable(false);
        view = View.inflate(HomeActivity.this, R.layout.dialog_setup_psd, null);
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
                    ToastUtils.showToast(HomeActivity.this, "密码不能为空");
                } else if (!password.equals(password_confirm)) {
                    ToastUtils.showToast(HomeActivity.this, "密码不一致");
                } else {
                    if (password.length() >= 6) {
                        editor.putString("password", Md5Utils.encode(password));
                        editor.putInt("passwordlength", password.length());
                        editor.commit();
                        alertDialog.dismiss();
                        showEnterPasswordDialog();
                    } else {
                        ToastUtils.showToast(HomeActivity.this, "密码不能少于6位");
                    }


                }
            }
        });
        dialog.setView(view);
        alertDialog = dialog.show();
    }

    /**
     * 初始化控件
     */
    private void init_view() {
        editor = sp.edit();
        gridView = (GridView) findViewById(R.id.gv_home);
        for (int i = 0; i < module_names.length; i++) {
            Module module = new Module();
            module.setModule_name(module_names[i]);
            module.setModule_img(module_imgs[i]);
            olist.add(module);
        }
        adapter = new GridViewAdapter(HomeActivity.this, olist);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(onItemlistener);

    }


    /**
     * @param keyCode
     * @param event
     * @return 对返回键的监听
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
        }
        return true;
    }

    /**
     * 利用计时器实现双击返回键退出应用程序功能
     */
    private boolean isStop = false;

    public void exit() {
        if (!isStop) {
            ToastUtils.showToast(HomeActivity.this, "再点击一次退出应用程序");
            isStop = true;
            new Timer().schedule(new TimerTask() {

                public void run() {
                    isStop = false;
                }
            }, 2000);

        } else {
            removeAllActivity();
            finish();
        }
    }
}
