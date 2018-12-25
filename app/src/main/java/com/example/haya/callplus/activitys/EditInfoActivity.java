package com.example.haya.callplus.activitys;


import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.haya.callplus.R;
import com.example.haya.callplus.Utils.DBUtils;
import com.example.haya.callplus.Utils.Utils;
import com.example.haya.callplus.beans.Contact;
import com.example.haya.callplus.data.Data;
import com.ninetripods.aopermission.permissionlib.annotation.NeedPermission;

import java.io.FileNotFoundException;

public class EditInfoActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_SELECT_PHOTO = 2;

    private Bitmap bitmap;  //存图片
    private EditText editTextName, editTextPhone, editTextPhone2, editTextQQ, editTextEmail, editTextGroup;
    private ImageView imageViewAvatar;
    private RadioButton radioButtonMan, radioButtonWomen;
    private Contact c;
    private Contact cOld;
    private String mode;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

        c = new Contact();
        initView();
        chooseMode();
    }

    public void add() {
        boolean flag = true;
        if (getContactFromView()) {
                DBUtils.insert(c);
            for (String groupName: Data.groupName) {
                if (groupName.equals(c.getGroup_name())) {
                    flag = false;
                    break;
                }
            }
            if (flag) DBUtils.insertGroup(c.getGroup_name());
            Data.sync();

            finish();
        }
    }

    private void chooseMode() {
        //获取模式添加或编辑模式
        intent = getIntent();
        mode = intent.getStringExtra("mode");
        if (mode.equals("edit")) {
            Toast.makeText(EditInfoActivity.this, "编辑", Toast.LENGTH_SHORT).show();
            edit();
        }
    }

    private void edit() {

        cOld = (Contact) intent.getSerializableExtra("obj");
        editTextName.setText(cOld.getName());
        editTextPhone.setText(cOld.getPhone());
        editTextPhone2.setText(cOld.getPhone2());
        editTextQQ.setText(cOld.getqq());
        editTextEmail.setText(cOld.getEmail());

        if (cOld.getAvatar() != null) {
            imageViewAvatar.setImageBitmap(cOld.getAvatar());
        }
        if (cOld.getSex() != null) {
            if (cOld.getSex().equals("男")) {
                radioButtonMan.setChecked(true);
            } else {
                radioButtonWomen.setChecked(true);
            }
        }
    }

    private boolean getContactFromView() {
        if (editTextName.getText().toString().trim().length() == 0
                || editTextPhone.getText().toString().length() == 0) {
            Toast.makeText(EditInfoActivity.this, "姓名、手机号不能为空", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!Utils.isNumeric(editTextPhone.getText().toString().trim())
                || !Utils.isNumeric(editTextPhone2.getText().toString().trim())) {
            Toast.makeText(EditInfoActivity.this, "号码无效", Toast.LENGTH_SHORT).show();
            return false;
        } else if (Utils.isEmoji(editTextName.getText().toString().trim())) {
            Toast.makeText(EditInfoActivity.this, "名字包含emoji", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!Utils.isQQ(editTextQQ.getText().toString().trim())) {
            Toast.makeText(EditInfoActivity.this, "QQ无效", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!Utils.isEmail(editTextEmail.getText().toString().trim())) {
            Toast.makeText(EditInfoActivity.this, "无效邮箱", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            if (radioButtonMan.isChecked()) {
                c.setSex("男");
            } else if (radioButtonWomen.isChecked()) {
                c.setSex("女");
            }
            c.setName(editTextName.getText().toString().trim());
            c.setPhone(editTextPhone.getText().toString().trim());
            c.setPhone2(editTextPhone2.getText().toString().trim());
            c.setqq(editTextQQ.getText().toString().trim());
            c.setEmail(editTextEmail.getText().toString().trim());
            if ("".equals(editTextGroup.getText().toString().trim()))
                c.setGroup_name("默认");
            else
                c.setGroup_name(editTextGroup.getText().toString().trim());
            if (bitmap != null) {
                c.setAvatar(Utils.getBytes(bitmap));
            }
        }
        return true;
    }

    private void initView() {
        editTextName = findViewById(R.id.edit_name);
        editTextPhone = findViewById(R.id.edit_phone);
        editTextPhone2 = findViewById(R.id.edit_phone2);
        editTextQQ = findViewById(R.id.edit_qq);
        editTextEmail = findViewById(R.id.edit_email);
        editTextGroup = findViewById(R.id.edit_group);
        radioButtonMan = findViewById(R.id.radio_man);
        radioButtonWomen = findViewById(R.id.radio_women);
        imageViewAvatar = findViewById(R.id.id_add_avatar);
        //为头像单击注册菜单
        imageViewAvatar.setOnClickListener(v -> {
            registerForContextMenu(imageViewAvatar);
            openContextMenu(imageViewAvatar);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_SELECT_PHOTO:
                //获取图片
                if (resultCode == RESULT_OK && data != null) {
                    ContentResolver cr = EditInfoActivity.this.getContentResolver();
                    Uri uri = data.getData();
                    try {
                        bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    imageViewAvatar.setImageBitmap(bitmap);
                }
                break;
            case REQUEST_IMAGE_CAPTURE:
                //获取拍照后的照片
                if (resultCode == RESULT_OK && data != null) {
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    imageViewAvatar.setImageBitmap(imageBitmap);
                    c.setAvatar(Utils.getBytes(imageBitmap));
                }
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                //打开相册
                album();
                break;
            case 2:
                //打开相机
                camra();
                break;
        }
        return super.onContextItemSelected(item);
    }
    @NeedPermission(value = {Manifest.permission.CAMERA})
    private void camra() {
        Toast.makeText(EditInfoActivity.this, "相机", Toast.LENGTH_SHORT).show();
        Utils.openCamera(EditInfoActivity.this, REQUEST_IMAGE_CAPTURE);
    }
    @NeedPermission(value = {Manifest.permission.WRITE_EXTERNAL_STORAGE})
    private void album() {
        Toast.makeText(EditInfoActivity.this, "相册", Toast.LENGTH_SHORT).show();
        Utils.openSelectPhoto(EditInfoActivity.this, REQUEST_SELECT_PHOTO);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, 1, 0, "替换");
        menu.add(0, 2, 0, "拍照");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save, menu);
        MenuItem saveItem = menu.findItem(R.id.save);
        saveItem.setOnMenuItemClickListener(item -> {
            if (mode.equals("add")) {
                add();
            }
            if (mode.equals("edit")) {
                save();
                Toast.makeText(EditInfoActivity.this, "保存", Toast.LENGTH_SHORT).show();
            }
            return false;
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void save() {
        if (getContactFromView()) {
            DBUtils.update(c, cOld);
            finish();
        }
    }
}