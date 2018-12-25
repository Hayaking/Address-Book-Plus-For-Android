package com.example.haya.callplus.beans;

import android.graphics.Bitmap;

import com.example.haya.callplus.Utils.Utils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Arrays;

public class Contact implements Serializable{
    @Expose()
    @SerializedName("name")
    private String name;
    @Expose()
    @SerializedName("sex")
    private String sex;
    @Expose()
    @SerializedName("phone")
    private String phone;
    @Expose()
    @SerializedName("phone2")
    private String phone2;
    @Expose(serialize = false,deserialize = false)
    private byte[] avatar;
    @Expose()
    @SerializedName("qq")
    private String qq;
    @Expose()
    @SerializedName("email")
    private String email;
    @Expose()
    @SerializedName("group_name")
    private String group_name;

    public Contact() {
    }
    public Contact(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }
    public Contact(String name, String sex, String phone, String phone2, String qq, String email, String group_name) {
        this.name = name;
        this.sex = sex;
        this.phone = phone;
        this.phone2 = phone2;
        this.qq = qq;
        this.email = email;
        this.group_name = group_name;
    }
    public Contact(String name, String sex, String phone, String phone2, byte[] avatar, String qq, String email, String group_name) {
        this.name = name;
        this.sex = sex;
        this.phone = phone;
        this.phone2 = phone2;
        this.avatar = avatar;
        this.qq = qq;
        this.email = email;
        this.group_name = group_name;
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

    public String getEmail() {
        if (email==null) return "空";

        return email;
    }

    public void setEmail(String email) {
        if (email == null || email.length() == 0) {
            this.email = null;
        } else {
            this.email = email;
        }
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
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

    public void setPhone(String num) {
        this.phone = num.replaceAll("-", "");
    }

    public String getPhone2() {
        if (phone2==null) return "空";
        return phone2;
    }

    public void setPhone2(String phone2) {
        if (phone2 == null || phone2.length() == 0) {
            this.phone2 = null;
        } else {
            this.phone2 = phone2.replaceAll("-", "");
        }
    }

    public String getSex() {
        if (sex==null) return "空";
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getqq() {
        if (qq==null) return "空";

        return qq;
    }

    public void setqq(String qq) {
        if (qq == null || qq.length() == 0) {
            this.qq = null;
        } else {
            this.qq = qq;
        }
    }

}
