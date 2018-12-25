package com.example.haya.callplus.activitys;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.haya.callplus.R;
import com.uuzuche.lib_zxing.activity.CodeUtils;

public class QrCodeActivity extends AppCompatActivity {
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);
        imageView = findViewById(R.id.info_qrcode);
        Intent intent = getIntent();
        String json = intent.getStringExtra("json");
        imageView.setImageBitmap(CodeUtils.createImage(json, 400, 400, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher)));
    }
}
