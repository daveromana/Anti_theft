package com.jju.feng.anti_theft.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jju.feng.anti_theft.R;
import com.jju.feng.anti_theft.adapter.ContactsAdapter;
import com.jju.feng.anti_theft.utils.ContactInfoUtils;

import java.util.List;

public class selectContactActivity extends BaseActivity {
    private ListView lv_contacts;
    private List<ContactInfoUtils.ContactInfo> infos;
    private ContactsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contact);
        lv_contacts = (ListView) findViewById(R.id.lv_contacts);
        //获取手机联系人的信息
        infos = ContactInfoUtils.getContactInfos(this);
        for (int i = 0; i < infos.size(); i++) {
            Log.e("tag", "姓名："+infos.get(i).getName() + " 电话：" + infos.get(i).getPhone() + " email:" + infos.get(i).getEmail());
        }
        adapter = new ContactsAdapter(infos, this);
        lv_contacts.setAdapter(adapter);
        lv_contacts.setOnItemClickListener(listener);
    }

    /**
     * listview的监听事件
     */
    private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String phone = infos.get(position).getPhone();
            Intent data = new Intent();
            data.putExtra("phone", phone);
            setResult(0, data);
            finish();
        }
    };
}
