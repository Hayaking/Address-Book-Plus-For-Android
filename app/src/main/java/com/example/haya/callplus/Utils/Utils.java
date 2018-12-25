package com.example.haya.callplus.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.haya.callplus.R;
import com.example.haya.callplus.activitys.EditInfoActivity;

import java.io.ByteArrayOutputStream;
import java.util.regex.Pattern;

public class Utils {
    //检查权限
    public static boolean checkPermission(Context context, String permission) {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(context, permission);
    }
    /**
     * @Description: setAddItem
     * @param context
     * @param: menu
     * @param: id
     * @return: void
     * @Author: Haya
     * @Date: 2018/11/5
     */
    public static void setAddItem(final Activity context, Menu menu, int id) {
        MenuItem addItem = menu.findItem(id);
        addItem.setOnMenuItemClickListener(item -> {
            Intent intent = new Intent(context, EditInfoActivity.class);
            intent.putExtra("mode", "add");
            context.startActivity(intent);
            return true;
        });
    }


    //获得searchview
    public static SearchView getSearchView(Activity context, Menu menu, int id, final ListView listView) {
        MenuItem searchItem = menu.findItem(id);
        SearchView mSearchView = (SearchView) searchItem.getActionView();
        try {
            //获得搜索框view
            mSearchView.setIconified(false);//设置searchView处于展开状态
            //mSearchView.onActionViewExpanded();// 当展开无输入内容的时候，没有关闭的图标
            mSearchView.onActionViewCollapsed();
            //mSearchView.setIconifiedByDefault(false);//默认为true在框内，设置false则在框外
            mSearchView.setSubmitButtonEnabled(true);//显示提交按钮
            mSearchView.clearFocus();            //关闭焦点
            mSearchView.setQueryHint("输入查询内容");
            mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                //当搜索框文本改变
                @Override
                public boolean onQueryTextChange(String newText) {
                    if (!(TextUtils.isEmpty(newText))) {
                        listView.setFilterText(newText);
                    } else {
                        listView.clearTextFilter();
                    }
                    return false;
                }

                //当搜索框文本提交
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }
            });
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        return mSearchView;
    }



    //打开相机
    public static void openCamera(Activity context, int requestCode) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivityForResult(intent, requestCode);
        }
    }

    //打开图库
    public static void openSelectPhoto(Activity context, int requestCode) {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        context.startActivityForResult(intent, requestCode);//打开相册
    }



    private static final int PERMISSION_READ_CONTACTS_CODE = 2;

    private Utils() {
    }
    //判断是否为数字
    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    //判断是否包含emoji
    public static boolean isEmoji(String str){
        String regex = "(?:[\uD83C\uDF00-\uD83D\uDDFF]|[\uD83E\uDD00-\uD83E\uDDFF]|[\uD83D\uDE00-\uD83D\uDE4F]|[\uD83D\uDE80-\uD83D\uDEFF]|[\u2600-\u26FF]\uFE0F?|[\u2700-\u27BF]\uFE0F?|\u24C2\uFE0F?|[\uD83C\uDDE6-\uD83C\uDDFF]{1,2}|[\uD83C\uDD70\uD83C\uDD71\uD83C\uDD7E\uD83C\uDD7F\uD83C\uDD8E\uD83C\uDD91-\uD83C\uDD9A]\uFE0F?|[\u0023\u002A\u0030-\u0039]\uFE0F?\u20E3|[\u2194-\u2199\u21A9-\u21AA]\uFE0F?|[\u2B05-\u2B07\u2B1B\u2B1C\u2B50\u2B55]\uFE0F?|[\u2934\u2935]\uFE0F?|[\u3030\u303D]\uFE0F?|[\u3297\u3299]\uFE0F?|[\uD83C\uDE01\uD83C\uDE02\uD83C\uDE1A\uD83C\uDE2F\uD83C\uDE32-\uD83C\uDE3A\uD83C\uDE50\uD83C\uDE51]\uFE0F?|[\u203C\u2049]\uFE0F?|[\u25AA\u25AB\u25B6\u25C0\u25FB-\u25FE]\uFE0F?|[\u00A9\u00AE]\uFE0F?|[\u2122\u2139]\uFE0F?|\uD83C\uDC04\uFE0F?|\uD83C\uDCCF\uFE0F?|[\u231A\u231B\u2328\u23CF\u23E9-\u23F3\u23F8-\u23FA]\uFE0F?)";
        str = str.replaceAll("\\d", "");
        int len = str.length();
        if (len ==0){
            return false;
        }
        for (int i=0;i<len/2;i++){
            String substr = str.substring(i*2,(i+1)*2);
            if (Pattern.matches(regex, substr)){
                return true;
            }
        }
        return false;
    }
    public static boolean isEmail(String str){
        if (str.length() ==0 ){
            return true;
        }
        String regex = "^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
        return Pattern.matches(regex, str);
    }
    public static boolean isQQ(String str){
        if (str.length() ==0 ){
            return true;
        }
        String  regex ="[1-9][0-9]{4,14}";
        return Pattern.matches(regex, str);
    }

    public static byte[] getBytes(Bitmap bitmap){
        //实例化字节数组输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, baos);//压缩位图
        return baos.toByteArray();//创建分配字节数组
    }
    public static Bitmap getBitmap(byte[] data){
        return BitmapFactory.decodeByteArray(data, 0, data.length);//从字节数组解码位图
    }
}
