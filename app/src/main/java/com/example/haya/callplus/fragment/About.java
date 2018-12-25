package com.example.haya.callplus.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.haya.callplus.R;
import com.example.haya.callplus.activitys.InfoActivity;
import com.example.haya.callplus.activitys.QrCodeActivity;
import com.example.haya.callplus.data.Data;
import com.google.gson.Gson;
import com.uuzuche.lib_zxing.activity.CodeUtils;

public class About extends Fragment {
    private ImageButton qrBt;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        qrBt = getView().findViewById(R.id.about_qrbt);
    }

    public ImageButton getQrBt(Context context) {
        qrBt.setOnClickListener(v -> {
            Gson gson = new Gson();
            String json = gson.toJson(Data.self);
            Intent intent = new Intent(context, QrCodeActivity.class);
            intent.putExtra("json", json);
            startActivity(intent);
        });
        return qrBt;
    }

}
