package com.example.haya.callplus.activitys;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.haya.callplus.R;
import com.example.haya.callplus.adapter.InfoAdapter;
import com.example.haya.callplus.beans.Contact;
import com.example.haya.callplus.data.Data;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ninetripods.aopermission.permissionlib.annotation.NeedPermission;
import com.ninetripods.aopermission.permissionlib.annotation.PermissionCanceled;
import com.ninetripods.aopermission.permissionlib.annotation.PermissionDenied;
import com.ninetripods.aopermission.permissionlib.bean.CancelBean;
import com.ninetripods.aopermission.permissionlib.bean.DenyBean;

import java.util.ArrayList;
import java.util.Arrays;

public class InfoActivity extends AppCompatActivity {
    private ImageView avatar;
    private TextView name;
    private Contact contact;
    private ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        initView();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        int groupPosition = intent.getIntExtra("groupPosition",0);
        int childPosition = intent.getIntExtra("childPosition",0);
        contact = Data.infos.get(Data.groupName.get(groupPosition)).get(childPosition);
        ArrayList<String> info = new ArrayList<>();
        info.add(contact.getPhone());
        info.add(contact.getPhone2());
        info.add(contact.getSex());
        info.add(contact.getEmail());
        list.setAdapter(new InfoAdapter(InfoActivity.this,info));
        name.setText(contact.getName());
        if (contact.getAvatar()!=null)
            avatar.setImageBitmap(contact.getAvatar());
        else
            avatar.setImageResource(R.drawable.head);
    }

    private void initView() {
        getSupportActionBar().hide();

        avatar = findViewById(R.id.info_avator);
        name = findViewById(R.id.info_name);
        list = findViewById(R.id.info_list);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("haya");
        toolbar.inflateMenu(R.menu.info_toolbar_menu);

        toolbar.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            switch (id) {
                case R.id.menu_info_qrcode:
                    Toast.makeText(InfoActivity.this, "二维码", Toast.LENGTH_SHORT).show();
                    GsonBuilder builder = new GsonBuilder();
                    builder.excludeFieldsWithoutExposeAnnotation();
                    Gson gson = builder.create();
                    String json = gson.toJson(contact);
                    Intent intent = new Intent(InfoActivity.this, QrCodeActivity.class);
                    intent.putExtra("json", json);
                    startActivity(intent);
                    break;
                case R.id.menu_info_edit:
                    Intent intent1 = new Intent(InfoActivity.this, EditInfoActivity.class);
                    intent1.putExtra("mode", "edit");
                    intent1.putExtra("obj", contact);
                    startActivity(intent1);
            }
            return true;
        });

    }


    @NeedPermission( {Manifest.permission.CALL_PHONE})
    public void call(View view) {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);//设置活动类型
        intent.setData(Uri.parse("tel:"+contact.getPhone()));//设置电话号
//        Intent intent = new Intent(InfoActivity.this, CallActivity.class);
//        //传递号码
//        intent.putExtra(Intent.EXTRA_PHONE_NUMBER, contact.getPhone());
//        //传递类型
//        intent.putExtra(Intent.EXTRA_MIME_TYPES, PhoneCallService.CallType.CALL_OUT);

        startActivity(intent);
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
    @NeedPermission( {Manifest.permission.SEND_SMS})
    public void msg(View view) {
        Intent intent = new Intent(InfoActivity.this, MessageActivity.class);
        startActivity(intent);
//        SmsManager smsManager = SmsManager.getDefault();
//        smsManager.sendTextMessage("17637999428",null,"textSms",null,null);
    }
}
