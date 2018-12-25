package com.example.haya.callplus.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.haya.callplus.R;
import com.example.haya.callplus.Utils.Utils;
import com.example.haya.callplus.beans.HistoricalRecord;
import com.example.haya.callplus.data.Data;

import java.util.List;

public class AdapterHistory extends RecyclerView.Adapter<AdapterHistory.ViewHolder>  {
    private List<HistoricalRecord> backList;
    static float width;
    public AdapterHistory( List<HistoricalRecord> list) {
        this.backList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.history_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(Data.historicalRecord.get(position).getName());
        holder.phone.setText(Data.historicalRecord.get(position).getPhone());
        if (Data.historicalRecord.get(position).getAvatar()!=null)
            holder.avator.setImageBitmap(Data.historicalRecord.get(position).getAvatar());
        else
            holder.avator.setImageResource(R.drawable.head);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return Data.historicalRecord.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView phone;
        ImageView avator;
        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.his_name);
            phone = itemView.findViewById(R.id.his_phone);
            avator = itemView.findViewById(R.id.his_avatar);
            width = phone.getWidth();
        }
    }
}
