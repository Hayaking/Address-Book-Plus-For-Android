package com.example.haya.callplus.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.haya.callplus.R;
import com.example.haya.callplus.data.Data;

import java.util.Arrays;
import java.util.List;

public class InfoAdapter extends BaseAdapter {
    private final LayoutInflater layoutInflater;
    private Context context;
    private List<String> info;
    public InfoAdapter(Context context, List info) {
        this.context = context;
        this.info = info;
        layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //Log.println(Log.INFO, "hayainfos", Arrays.toString(info.toArray()));
    }

    @Override
    public int getCount() {
        return info.size();
    }

    @Override
    public Object getItem(int position) {
        return info.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.info_list_item, null);

        }
            ImageView ico = convertView.findViewById(R.id.info_icon);
            TextView contain = convertView.findViewById(R.id.info_contain);
            contain.setText(info.get(position).toString());
            ico.setImageResource(Data.ico.get(position));
        return convertView;
    }


}
