package com.example.haya.callplus.beans;

import android.graphics.Bitmap;

import com.example.haya.callplus.Utils.Utils;

public class HistoricalRecord {
    private String name;
    private String phone;
    private byte[] avatar;
    int orderNum;

    public HistoricalRecord(String name, String phone,byte[] avatar, int orderNum) {
        this.name = name;
        this.phone = phone;
        this.avatar = avatar;
        this.orderNum = orderNum;
    }

    public Bitmap getAvatar() {
        if (avatar!=null){
            return Utils.getBitmap(avatar);
        }else{
            return  null;
        }
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

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

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }
}
