package com.jju.feng.anti_theft.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jju.feng.anti_theft.R;
import com.jju.feng.anti_theft.domain.Module;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/5 0005.
 */

public class GridViewAdapter extends BaseAdapter {
    private List<Module> olist = new ArrayList<Module>();
    private Context ocontext;
    private LayoutInflater oinflater;

    public GridViewAdapter(Context ocontext, List<Module> olist) {
        this.ocontext = ocontext;
        this.olist = olist;
        oinflater = LayoutInflater.from(ocontext);
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
        ViewHolder oholder = null;
        if (convertView == null) {
            oholder = new ViewHolder();
            convertView = LayoutInflater.from(ocontext).inflate(R.layout.item_gridview, null);
            oholder.module_img = (ImageView) convertView.findViewById(R.id.grid_item_image);
            oholder.module_name = (TextView) convertView.findViewById(R.id.grid_item_tv);
            convertView.setTag(oholder);
        } else {
            oholder = (ViewHolder) convertView.getTag();
        }
        oholder.module_name.setText(olist.get(position).getModule_name());
        oholder.module_img.setImageResource(olist.get(position).getModule_img());
        return convertView;
    }

    private class ViewHolder {
        private TextView module_name;
        private ImageView module_img;
    }
}
