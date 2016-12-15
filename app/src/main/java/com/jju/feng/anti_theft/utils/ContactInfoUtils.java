package com.jju.feng.anti_theft.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/9 0009.
 */

public class ContactInfoUtils {

    /**
     * 联系人信息的工具类
     *
     * @param context 上下文
     * @return
     */
    public static List<ContactInfo> getContactInfos(Context context) {
        //内容提供者的解析器
        ContentResolver resolver = context.getContentResolver();
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri datauri = Uri.parse("content://com.android.contacts/data");
        Cursor cursor = resolver.query(uri, new String[]{"contact_id"}, null, null, null);
        List<ContactInfo> contactInfos = new ArrayList<ContactInfo>();
        while (cursor.moveToNext()) {
            String id = cursor.getString(0);
            Log.i("TAG", "getContactInfos" + id);
            if (id != null) {
                ContactInfo info = new ContactInfoUtils().new ContactInfo();
                Cursor datacursor = resolver.query(datauri, new String[]{"data1", "mimetype"}, "raw_contact_id=?", new String[]{id}, null);

                while (datacursor.moveToNext()) {
                    String data1 = datacursor.getString(datacursor.getColumnIndex("data1"));
                    String mimetype = datacursor.getString(datacursor.getColumnIndex("mimetype"));
                    if ("vnd.android.cursor.item/phone_v2".equals(mimetype)) {
                        info.phone = data1;
                    } else if ("vnd.android.cursor.item/email_v2".equals(mimetype)) {
                        info.email = data1;
                    } else if ("vnd.android.cursor.item/name".equals(mimetype)) {
                        info.name = data1;
                    }

                }
                if (info.phone != null) {
                    contactInfos.add(info);
                } else {
                    info = null;
                }
            }
        }
        return contactInfos;
    }

    public class ContactInfo {
        String name;
        String phone;
        String email;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
