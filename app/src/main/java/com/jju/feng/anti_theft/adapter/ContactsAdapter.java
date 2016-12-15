package com.jju.feng.anti_theft.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jju.feng.anti_theft.R;
import com.jju.feng.anti_theft.utils.ContactInfoUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/12/9 0009.
 */

public class ContactsAdapter extends BaseAdapter {
    private List<ContactInfoUtils.ContactInfo> olist;
    private Context oContext;
    private LayoutInflater oInflater;

    public ContactsAdapter(List<ContactInfoUtils.ContactInfo> olist, Context oContext) {
        this.olist = olist;
        this.oContext = oContext;
        oInflater = LayoutInflater.from(oContext);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder oHolder = null;
        if (convertView == null) {
            oHolder = new ViewHolder();
            convertView = LayoutInflater.from(oContext).inflate(R.layout.item_contact, null);
            oHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            oHolder.tv_phong = (TextView) convertView.findViewById(R.id.tv_phone);
            convertView.setTag(oHolder);
        } else {
            oHolder = (ViewHolder) convertView.getTag();
        }
        oHolder.tv_name.setText(olist.get(position).getName());
        oHolder.tv_phong.setText(olist.get(position).getPhone());
        return convertView;
    }

    private class ViewHolder {
        TextView tv_name;
        TextView tv_phong;

    }
}
