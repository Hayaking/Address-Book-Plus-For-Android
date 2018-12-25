package com.example.haya.callplus.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.haya.callplus.R;
import com.example.haya.callplus.Utils.DBUtils;
import com.example.haya.callplus.activitys.MainActivity;
import com.example.haya.callplus.activitys.MessageActivity;
import com.example.haya.callplus.adapter.AdapterHistory;
import com.example.haya.callplus.beans.HistoricalRecord;
import com.example.haya.callplus.data.Data;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.List;

public class History extends Fragment {
    private SwipeMenuRecyclerView listView;
    private AdapterHistory adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        listView = getView().findViewById(R.id.history_list);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter!=null)
            adapter.notifyDataSetChanged();
    }

    public SwipeMenuRecyclerView getListView(final Context context) {
        adapter = new AdapterHistory(Data.historicalRecord);
        listView.setLayoutManager(new LinearLayoutManager(context));
        listView.setSwipeMenuCreator((swipeLeftMenu, swipeRightMenu, viewType) -> {
            int size=180;
            SwipeMenuItem deleteItem = new SwipeMenuItem(context)
                    .setBackgroundColor(Color.RED)
                    .setText("删除") // 文字。
                    .setTextColor(Color.BLACK) // 文字颜色。
                    .setTextSize(16) // 文字大小。
                    .setWidth(size)
                    .setHeight(size);
            swipeRightMenu.addMenuItem(deleteItem);// 添加一个按钮到右侧侧菜单。.

        });
        //设置侧滑菜单的点击事件
        listView.setSwipeMenuItemClickListener(menuBridge -> {
            menuBridge.closeMenu();
            //int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。0是左，右是1，暂时没有用到
            int position = menuBridge.getAdapterPosition(); // RecyclerView的Item的position。
            //int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。
            DBUtils.deleteHistoricalRecord(Data.historicalRecord.get(position));
            Data.historicalRecord.remove(position);
            Toast.makeText(context, "删除"+position, Toast.LENGTH_SHORT).show();
            adapter.notifyDataSetChanged();
        });
        listView.setSwipeItemClickListener((itemView, position) -> {
            Toast.makeText(context, ""+position, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, MessageActivity.class);
            startActivity(intent);
        });
        listView.setAdapter(adapter);
        return listView;
    }
}
