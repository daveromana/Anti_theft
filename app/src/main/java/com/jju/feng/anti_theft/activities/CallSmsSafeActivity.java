package com.jju.feng.anti_theft.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.jju.feng.anti_theft.R;
import com.jju.feng.anti_theft.adapter.CallSmsSafeAdapter;
import com.jju.feng.anti_theft.db.dao.BlackNumberDao;
import com.jju.feng.anti_theft.domain.BlackNumberInfo;
import com.jju.feng.anti_theft.utils.ToastUtils;

import java.util.List;

public class CallSmsSafeActivity extends BaseActivity {
    private ListView lv_blacknum;
    private BlackNumberDao dao;
    private CallSmsSafeAdapter adapter;

    /**
     * 全部的黑名单号码信息
     */
    private List<BlackNumberInfo> infos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_sms_safe);
        lv_blacknum = (ListView) findViewById(R.id.lv_blacknum);
        dao = new BlackNumberDao(this);
        infos = dao.findAll();
        adapter = new CallSmsSafeAdapter(infos, this);
        lv_blacknum.setAdapter(adapter);
    }

    /**
     * 添加黑名单的点击事件
     *
     * @param view 当前按钮
     */
    public void btn_addBlackNumber(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = View.inflate(this, R.layout.dialog_add_blacknumber, null);
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        final EditText et_black_num = (EditText) dialogView.findViewById(R.id.et_black_num);
        final RadioGroup rg_mode = (RadioGroup) dialogView.findViewById(R.id.rg_mode);
        Button btn_ok = (Button) dialogView.findViewById(R.id.btn_ok);
        Button btn_cancel = (Button) dialogView.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = et_black_num.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    ToastUtils.showToast(CallSmsSafeActivity.this, "号码不能为空");
                    return;
                }
                int id = rg_mode.getCheckedRadioButtonId();
                String mode = "3";
                switch (id) {
                    case R.id.radio_phone:
                        mode = "1";
                        break;
                    case R.id.radio_sms:
                        mode = "2";
                        break;
                    case R.id.radio_all:
                        mode = "3";
                        break;
                }
                boolean result = dao.add(phone, mode);
                if (result) {
                    //添加成功
                    ToastUtils.showToast(CallSmsSafeActivity.this, "添加成功");
                    BlackNumberInfo object = new BlackNumberInfo();
                    object.setMode(mode);
                    object.setPhone(phone);
                    infos.add(0, object);
                    //通知listview界面更新
                    adapter.notifyDataSetChanged();
                } else {
                    ToastUtils.showToast(CallSmsSafeActivity.this, "添加失败");
                }
                dialog.dismiss();

            }
        });
        dialog.setView(dialogView, 0, 0, 0, 0);
        dialog.show();
    }
}
