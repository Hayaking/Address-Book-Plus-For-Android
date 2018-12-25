package com.example.haya.callplus.activitys;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.haya.callplus.R;
import com.example.haya.callplus.Utils.DBUtils;
import com.example.haya.callplus.Utils.Utils;
import com.example.haya.callplus.beans.Contact;
import com.example.haya.callplus.data.Data;
import com.example.haya.callplus.fragment.Contacts;
import com.example.haya.callplus.fragment.History;
import com.example.haya.callplus.fragment.About;
import com.google.gson.Gson;
import com.ninetripods.aopermission.permissionlib.annotation.NeedPermission;
import com.ninetripods.aopermission.permissionlib.annotation.PermissionCanceled;
import com.ninetripods.aopermission.permissionlib.annotation.PermissionDenied;
import com.ninetripods.aopermission.permissionlib.bean.CancelBean;
import com.ninetripods.aopermission.permissionlib.bean.DenyBean;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private FragmentTransaction transition;
    private List<Fragment> fragments;
    private BottomNavigationView navigation;
    private MyFragment myFragment;
    private ExpandableListView lv_contact;
    private SwipeMenuRecyclerView historyListView;
    private ImageButton qrBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES);

        super.onCreate(savedInstanceState);
        //初始化二维码。。。
        ZXingLibrary.initDisplayOpinion(this);
        setContentView(R.layout.activity_main);
        new DBUtils(getApplicationContext());
        Data.sync();
        initView();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (lv_contact == null)
            lv_contact = myFragment.contacts.getListView(MainActivity.this,Data.infos);

        if (historyListView==null)
            historyListView = myFragment.history.getListView(MainActivity.this);
        if (qrBt == null)
            qrBt = myFragment.about.getQrBt(MainActivity.this);
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    private void initView() {
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        fragmentManager = getSupportFragmentManager();
        fragments = new ArrayList<>();
        myFragment = new MyFragment(new History(), new Contacts(), new About());

    }
    private class MyFragment{
        History history;
        Contacts contacts;
        About about;

        public MyFragment(History history, Contacts contacts, About about) {
            this.history = history;
            this.contacts = contacts;
            this.about = about;
            hideOthersFragment(contacts, true);
            hideOthersFragment(about, true);
            hideOthersFragment(history, true);
            fragments.add(history);
            fragments.add(contacts);
            fragments.add(about);
            hideOthersFragment(history,false);
        }
    }
    /*
    * 底部导航栏监听器
    * */
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    hideOthersFragment(myFragment.history, false);
                    return true;
                case R.id.navigation_dashboard:
                    hideOthersFragment(myFragment.contacts, false);
                    return true;
                case R.id.navigation_notifications:
                    hideOthersFragment(myFragment.about, false);
                    return true;
            }
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater()获得该activity的MenuInflater，并通过inflate函数设置R.menu.menu作为该activity的menu
        getMenuInflater().inflate(R.menu.menu, menu);
        Utils.getSearchView(MainActivity.this, menu, R.id.search, lv_contact);        //获得搜索框item
        setAddItem(menu, R.id.add);        //增加选项
        setScanItem(menu, R.id.scan);       //扫一扫
        return super.onCreateOptionsMenu(menu);
    }
    public void setAddItem(Menu menu, int id) {
        MenuItem addItem = menu.findItem(id);
        addItem.setOnMenuItemClickListener(item -> {
            Intent intent = new Intent(MainActivity.this, EditInfoActivity.class);
            intent.putExtra("mode", "add");
            startActivity(intent);
            return true;
        });
    }
    @NeedPermission(value = {Manifest.permission.CAMERA})
    public void setScanItem(Menu menu, int id) {
        MenuItem addItem = menu.findItem(id);
        addItem.setOnMenuItemClickListener(item -> {
            Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
            startActivityForResult(intent, Data.QR_CODE);
            return true;
        });
    }

    /**
     * 动态显示Fragment
     * @param showFragment 要增加的fragment
     * @param add          true：增加fragment；
     */
    private void hideOthersFragment(Fragment showFragment, boolean add) {
        transition = fragmentManager.beginTransaction();
        if (add)
            transition.add(R.id.main_container_content, showFragment);
        for (Fragment fragment : fragments) {
            if (showFragment.equals(fragment)) {
                transition.show(fragment);
            } else {
                transition.hide(fragment);
            }
        }
        transition.commit();
    }

    @PermissionCanceled
    public void dealCancelPermission(CancelBean bean) {
        Toast.makeText(this, "requestCode:" + bean.getRequestCode(), Toast.LENGTH_SHORT).show();
    }

    @PermissionDenied
    public void dealPermission(DenyBean bean) {
        Toast.makeText(this,
                "requestCode:" + bean.getRequestCode()+ ",Permissions: " + Arrays.toString(bean.getDenyList().toArray()), Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Data.QR_CODE:
                //处理扫描结果（在界面上显示）
                if (null != data) {
                    Bundle bundle = data.getExtras();
                    if (bundle == null)
                        return;
                    if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                        Gson gson = new Gson();
                        String json = bundle.getString(CodeUtils.RESULT_STRING);
//                        Toast.makeText(this, "解析结果:" + json, Toast.LENGTH_LONG).show();
                        Contact contact = gson.fromJson(json, Contact.class);
//                        Toast.makeText(this, "解析结果:" + contact.toString(), Toast.LENGTH_LONG).show();

                        DBUtils.insertGroup(contact.getGroup_name());
                        DBUtils.insert(contact);
                        Data.sync();
                    } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                        Toast.makeText(MainActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                    }

                    break;
                }

        }
    }
}
