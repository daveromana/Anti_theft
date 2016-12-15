package com.jju.feng.anti_theft.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jju.feng.anti_theft.R;
import com.jju.feng.anti_theft.db.dao.BlackNumberDao;
import com.jju.feng.anti_theft.domain.BlackNumberInfo;
import com.jju.feng.anti_theft.utils.ToastUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/12/9 0009.
 */

public class CallSmsSafeAdapter extends BaseAdapter {
    private BlackNumberDao dao;
    private List<BlackNumberInfo> olist;
    private Context oContext;

    public CallSmsSafeAdapter(List<BlackNumberInfo> olist, Context oContext) {
        this.olist = olist;
        this.oContext = oContext;
        dao = new BlackNumberDao(oContext);
    }

    @Override
    public int getCount() {
        return olist.size();
    }

    @Override
    public Object getItem(int position) {
        return olist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder oHolder = null;
        if (convertView == null) {
            oHolder = new ViewHolder();
            convertView = LayoutInflater.from(oContext).inflate(R.layout.item_callsmssafe, null);
            oHolder.tv_black_phone = (TextView) convertView.findViewById(R.id.tv_black_phone);
            oHolder.tv_black_mode = (TextView) convertView.findViewById(R.id.tv_black_mode);
            oHolder.iv_delete_blacknumber = (ImageView) convertView.findViewById(R.id.iv_delete_blacknumber);
            convertView.setTag(oHolder);
        } else {
            oHolder = (ViewHolder) convertView.getTag();
        }
        String mode = olist.get(position).getMode();
        if ("1".equals(mode)) {
            oHolder.tv_black_mode.setText("电话拦截");
        } else if ("2".equals(mode)) {
            oHolder.tv_black_mode.setText("短信拦截");
        } else if ("3".equals(mode)) {
            oHolder.tv_black_mode.setText("电话+短信");
        }
        final String phone = olist.get(position).getPhone();
        oHolder.tv_black_phone.setText(phone);
        oHolder.iv_delete_blacknumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(oContext);
                builder.setTitle("警告");
                builder.setMessage("确定删除这个黑名单号码么?");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //删除数据库的记录
                        boolean result = dao.delete(phone);
                        if (result) {
                            ToastUtils.showToast(oContext, "删除成功", 1000);
                            //删除ui界面的数据
                            olist.remove(position);
                            //刷新界面
                            notifyDataSetChanged();
                        } else {
                            ToastUtils.showToast(oContext, "删除失败", 1000);
                        }
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.show();
            }
        });
        return convertView;
    }

    private class ViewHolder {
        TextView tv_black_phone;
        TextView tv_black_mode;
        ImageView iv_delete_blacknumber;

    }
}
