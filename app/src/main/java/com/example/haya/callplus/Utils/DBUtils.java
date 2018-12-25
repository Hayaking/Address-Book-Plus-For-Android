package com.example.haya.callplus.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.haya.callplus.beans.Contact;
import com.example.haya.callplus.beans.HistoricalRecord;
import com.example.haya.callplus.data.Data;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBUtils {
    private static SQLiteDatabase db;

    public DBUtils(Context context) {
        db = MyOpenHelper.getSingDB(context).getReadableDatabase();
    }

    public static SQLiteDatabase getDB() {
        return db;
    }

    public static void insert(Contact c) {
        ContentValues values = new ContentValues();
        if (c.getAvatar() != null) {
            byte[] b = Utils.getBytes(c.getAvatar());
            values.put("avatar", b);
        }
        values.put("name", c.getName());
        values.put("sex", c.getSex());
        values.put("phone", c.getPhone());
        values.put("phone2", c.getPhone2());
        values.put("qq", c.getqq());
        values.put("email", c.getEmail());
        values.put("groupname", c.getGroup_name());
        db.insert("connectInfo", null, values);
    }

    public static void insertGroup(String groupName) {
        //插入组名不存在
        if (!Data.groupName.contains(groupName)) {
            ContentValues values = new ContentValues();
            values.put("groupname", groupName);
            db.insert("group_name", null, values);
        }
    }

    public static void update(Contact c, Contact cOld) {
        ContentValues values = new ContentValues();
        if (c.getAvatar() != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            c.getAvatar().compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            values.put("avatar", b);
        }
        values.put("name", c.getName());
        values.put("sex", c.getSex());
        values.put("phone", c.getPhone());
        values.put("phone2", c.getPhone2());
        values.put("qq", c.getqq());
        values.put("email", c.getEmail());
        values.put("groupname", c.getGroup_name());
        db.update("connectInfo", values, "name = ? and phone =?", new String[]{cOld.getName(), cOld.getPhone()});
    }

    public static void delete(Contact c) {
        Log.println(Log.INFO, "haya", "删除-姓名：" + c.getName() + " 号码：" + c.getPhone());
        String sql = String.format("delete from connectInfo where name = '%s'and phone = '%s'", c.getName(), c.getPhone());
        db.execSQL(sql);
    }

    public static Contact query(Contact c) {
        String sql = String.format("SELECT *from connectInfo where name = '%s'and phone = '%s'", c.getName(), c.getPhone());
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToNext()) {
            byte b[] = cursor.getBlob(cursor.getColumnIndex("avatar"));
            c = new Contact(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    b,
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7));
        }
        cursor.close();
        return c;
    }

    public static Map<String, List<Contact>> queryAll(){
        Map<String, List<Contact>> infos = new HashMap<>();
        List<String> groupName = new ArrayList<>();
        String queryGroup = "select * from group_name";
        Cursor cursor = db.rawQuery(queryGroup, null);
        while (cursor.moveToNext()) {
            groupName.add(cursor.getString(0));
        }
        for (String group: groupName) {
            Log.println(Log.INFO, "hayatest", group);
            String queryContact = String.format("select *from connectInfo where groupname = '%s'" , group);
            cursor = db.rawQuery(queryContact, null);
            infos.put(group, new ArrayList<Contact>());
            while (cursor.moveToNext()) {
                byte b[] = cursor.getBlob(cursor.getColumnIndex("avatar"));
                Contact c = new Contact(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        b,
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7));
                infos.get(group).add(c);
            }
        }
        return  infos;
    }

    public static ArrayList queryGroupName(){
        return new ArrayList(queryAll().keySet());
    }

    public static List<HistoricalRecord> queryHistoricalRecord() {
        String sql = "select * from history";
        Cursor cursor = db.rawQuery(sql, null);
        List<HistoricalRecord> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            list.add(
                    new HistoricalRecord(cursor.getString(0),
                                                cursor.getString(1),
                            cursor.getBlob(2),
                            cursor.getInt(3)
                                        ));
        }
        return list;
    }

    public static void insertHistoricalRecord(Contact contact) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", contact.getName());
        contentValues.put("phone", contact.getPhone());
        if (contact.getAvatar() != null) {
            byte[] b = Utils.getBytes(contact.getAvatar());
            contentValues.put("avatar", b);
        }
        db.insert("history", null, contentValues);
    }

    public static void deleteHistoricalRecord(HistoricalRecord historicalRecord) {
        String sql = "delete from history where name=\'" + historicalRecord.getName() + "\' and " + "phone=\'" + historicalRecord.getPhone()+"\'";
        db.execSQL(sql );
    }
}
