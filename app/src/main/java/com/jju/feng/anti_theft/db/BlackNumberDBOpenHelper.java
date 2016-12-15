package com.jju.feng.anti_theft.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/12/12 0012.
 */

public class BlackNumberDBOpenHelper extends SQLiteOpenHelper {
    /**
     * 创建一个应用程序的数据库，数据库的名称叫做anti_theft.db
     *
     * @param context
     */
    public BlackNumberDBOpenHelper(Context context) {
        super(context, "anti_thetf.db", null, 1);
    }

    /**
     * 数据库第一次被创建的时候调用下面的方法，适合做数据库表结构的初始化。
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //_id 数据库的主键，自增长
        //phone 黑名单电话号码
        //mode 拦截模式1.电话拦截2.短信拦截，3.全部拦截
        db.execSQL("create table blacknumberinfo (_id integer primary key autoincrement,phone varchar(20),mode varchar(2))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
