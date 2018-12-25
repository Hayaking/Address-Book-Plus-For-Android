package com.example.haya.callplus.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.haya.callplus.R;
import com.example.haya.callplus.Utils.DBUtils;
import com.example.haya.callplus.activitys.InfoActivity;
import com.example.haya.callplus.adapter.Adapter;
import com.example.haya.callplus.beans.Contact;
import com.example.haya.callplus.data.Data;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

public class Contacts extends Fragment {
    private ExpandableListView listView;
    private Adapter adapter;
    private Context context;
    private MenuInflater inflater;
    private Menu menu;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_contact, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        listView = getView().findViewById(R.id.list);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    public ExpandableListView getListView(Context context, Map<String, List<Contact>> infos) {
        this.context = context;
        adapter = new Adapter(context, infos);
        listView.setAdapter(adapter);
        listView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            openInfoActivity(context, groupPosition, childPosition);
            return false;
        });
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            setAdapterCheckBoxVisiblity(true);
            setDeleteWithCancelMenu();
            return false;
        });
        return listView;
    }
    /*
     * @param flag 控制checkbox可视化
     * */
    protected void setAdapterCheckBoxVisiblity(Boolean flag) {
        Adapter.visiblityCheckBox = flag;
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }
    /*
    * 设置actionbar菜单为删除 取消
    * */
    protected void setDeleteWithCancelMenu() {
        menu.clear();
        inflater.inflate(R.menu.contac_delete_menu, menu);
    }
    /*
    * 设置actionbar菜单为Mainactivity菜单
    * */
    protected void setActivityMenu() {
        menu.clear();
        inflater.inflate(R.menu.menu, menu);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        this.menu = menu;
        this.inflater = inflater;
    }
    /*
    * actionbar上菜单被点击
    * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_delete:
                for (int i = 0; i <listView.getChildCount() ; i++) {
                    View view = listView.getChildAt(i);
                    CheckBox checkbox = view.findViewById(R.id.id_checkbox);
                    /*
                    * checkbox不为空 防止因为groupitem没有checkbox造成空指针异常
                    * */
                    if (checkbox!=null && checkbox.isChecked()) {
                        String name = ((TextView)view.findViewById(R.id.id_name)).getText().toString();
                        String phone = ((TextView)view.findViewById(R.id.id_phone)).getText().toString();
                        DBUtils.delete(new Contact(name,phone));
                    }
                }
//                Snackbar.make(listView, "删除成功", Snackbar.LENGTH_LONG).setAction("撤销", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(context, "!!!!", Toast.LENGTH_SHORT).show();
//                    }
//                }).show();
                Data.sync();
                break;
            case R.id.menu_cancel:
                break;
        }
        setAdapterCheckBoxVisiblity(false);
        setActivityMenu();
        return super.onOptionsItemSelected(item);
    }

    /*
    * 打开Infoactivity
    * */
    private void openInfoActivity(Context context, int groupPosition, int childPosition) {
        Intent intent = new Intent(context, InfoActivity.class);
        DBUtils.insertHistoricalRecord(Data.infos.get(Data.groupName.get(groupPosition)).get(childPosition));
        Data.sync();
        intent.putExtra("groupPosition", groupPosition);
        intent.putExtra("childPosition", childPosition);
        context.startActivity(intent);
    }
}
