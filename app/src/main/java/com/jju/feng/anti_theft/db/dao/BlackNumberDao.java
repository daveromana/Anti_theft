package com.jju.feng.anti_theft.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jju.feng.anti_theft.db.BlackNumberDBOpenHelper;
import com.jju.feng.anti_theft.domain.BlackNumberInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 操作黑名单数据库，提供增删改查的方法
 * Created by Administrator on 2016/12/12 0012.
 */

public class BlackNumberDao {
    private BlackNumberDBOpenHelper helper;

    /**
     * 黑名单dao的构造方法
     *
     * @param context 上下文
     */
    public BlackNumberDao(Context context) {
        helper = new BlackNumberDBOpenHelper(context);
    }

    /**
     * 添加黑名单号码
     *
     * @param phone 黑名单号码
     * @param mode  拦截模式
     * @return
     */
    public boolean add(String phone, String mode) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("phone", phone);
        values.put("mode", mode);
        long id = db.insert("blacknumberinfo", null, values);
        db.close();
        if (id != -1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 删除黑名单号码
     *
     * @param phone 要删除的黑名单号码
     * @return true 删除成功，false 删除失败
     */
    public boolean delete(String phone) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int rowcount = db.delete("blacknumberinfo", "phone=?", new String[]{phone});
        db.close();
        if (rowcount == 0) {
            return false;
        } else {
            return true;
        }

    }

    /**
     * 修改黑名单号码
     *
     * @param phone   要修改的黑名单号码
     * @param newmode 新的拦截模式
     * @return 修改是否成功
     */
    public boolean update(String phone, String newmode) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("mode", newmode);
        int rowcount = db.update("blacknumberinfo", values, "phone=?", new String[]{phone});
        if (rowcount == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 查找黑名单的拦截模式
     *
     * @param phone 要查找的黑名单号码
     * @return 查找的黑名单的拦截模式，1.电话拦截2.短信拦截，3.全部拦截，如果返回null，代表不是黑名单号码，
     */
    public String findmode(String phone) {
        String mode = null;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("blacknumberinfo", null, "phone=?", new String[]{phone}, null, null, null);

        if (cursor.moveToNext()) {
            mode = cursor.getString(cursor.getColumnIndex("mode"));
        }
        cursor.close();
        db.close();
        return mode;
    }

    /**
     * 返回全部的黑名单号码信息，并保存到javaBean中
     *
     * @return
     */
    public List<BlackNumberInfo> findAll() {
        SQLiteDatabase db = helper.getReadableDatabase();
        //以id倒序方式排序
        Cursor cursor = db.query("blacknumberinfo", null, null, null, null, null, "_id desc");
        List<BlackNumberInfo> infos = new ArrayList<BlackNumberInfo>();
        while (cursor.moveToNext()) {
            BlackNumberInfo info = new BlackNumberInfo();
            //首先拿到phone的列数，然后通过该列拿到对应的值
            String phone = cursor.getString(cursor.getColumnIndex("phone"));
            String mode = cursor.getString(cursor.getColumnIndex("mode"));
            info.setPhone(phone);
            info.setMode(mode);
            infos.add(info);
        }
        cursor.close();
        db.close();
        return infos;
    }
}
