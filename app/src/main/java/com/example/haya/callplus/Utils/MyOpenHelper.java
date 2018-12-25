package com.example.haya.callplus.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MyOpenHelper extends SQLiteOpenHelper {
    private static MyOpenHelper singDB;
    //联系人表
    private final String SQL_createTable =
            "CREATE TABLE connectInfo("
                    + "name VARCHAR(10),"
                    + "sex CHAR(1),"
                    + "phone VARCHAR(15),"
                    + "phone2 VARCHAR(15),"
                    + "avatar BLOB,"
                    + "qq VARCHAR(12),"
                    + "email VARCHAR(20),"
                    + "groupname VARCHAR(20) REFERENCES group_name(groupname),"
                    + "PRIMARY KEY(name,phone));";
    //分组表
    private final String SQL_groupTable =
            "CREATE TABLE group_name("
                + "groupname VARCHAR(20) primary key,"
                + "count INTEGER)";
    //历史纪录表
    private final String SQL_historyTable =
            "CREATE TABLE history("
                    + "name varchar(10) ,"
                    + "phone VARCHAR(15) ,"
                    + "avatar BLOB,"
                    + "order_num INTEGER,"
                    + "FOREIGN KEY (name,phone,avatar) REFERENCES connectInfo(name, phone,avatar),"
                    + "primary key(name,phone))";

    private MyOpenHelper(Context context) {
        super(context, "haya.db", null, 1);
    }

    public static MyOpenHelper getSingDB(Context context) {
        if (singDB == null)
            singDB = new MyOpenHelper(context);
        return singDB;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_createTable);
        db.execSQL(SQL_groupTable);
        db.execSQL(SQL_historyTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
