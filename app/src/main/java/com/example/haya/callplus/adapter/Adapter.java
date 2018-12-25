package com.example.haya.callplus.adapter;


import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.haya.callplus.R;
import com.example.haya.callplus.beans.Contact;
import com.example.haya.callplus.data.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Adapter extends BaseExpandableListAdapter implements Filterable {
    public static Boolean visiblityCheckBox = false;
    private MyFilter mFilter;
    private LayoutInflater layoutInflater;
    private Context context;
    private Map<String, List<Contact>> infos;
    private Map<String, List<Contact>> backInfos;

    public Adapter(Context context, Map<String, List<Contact>> infos){
        this.context = context;
        this.infos = backInfos = Data.infos;
        layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new MyFilter();
        }
        return mFilter;

    }


    @Override
    public int getGroupCount() {
        return Data.groupName.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int size = Data.infos.get(Data.groupName.get(groupPosition)).size();
        return size;

    }

    @Override
    public Object getGroup(int groupPosition) {
        return Data.groupName.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return Data.infos.get(Data.groupName.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.contact_list_group_item, parent, false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.groupName =  convertView.findViewById(R.id.label_expand_group);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        groupViewHolder.groupName.setText(Data.groupName.get(groupPosition));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.contact_list_item, parent, false);
            childViewHolder = new ChildViewHolder();
            childViewHolder.avatar =  convertView.findViewById(R.id.id_avatar);
            childViewHolder.name = convertView.findViewById(R.id.id_name);
            childViewHolder.phone = convertView.findViewById(R.id.id_phone);
            childViewHolder.sex = convertView.findViewById(R.id.id_sex);
            childViewHolder.checkBox = convertView.findViewById(R.id.id_checkbox);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }

        Contact cur = Data.infos.get(Data.groupName.get(groupPosition)).get(childPosition);
        childViewHolder.name.setText(cur.getName());
        childViewHolder.sex.setText(cur.getSex());
        childViewHolder.phone.setText(cur.getPhone());
        if (cur.getAvatar()!=null)
            childViewHolder.avatar.setImageBitmap(cur.getAvatar());
        if (visiblityCheckBox) childViewHolder.checkBox.setVisibility(View.VISIBLE);
        else childViewHolder.checkBox.setVisibility(View.INVISIBLE);
        return convertView;
    }
    static class GroupViewHolder{
        TextView groupName;
    }
    static class ChildViewHolder{
        ImageView avatar;
        TextView name;
        TextView phone;
        TextView sex;
        CheckBox checkBox;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {

        return true;
    }
    class MyFilter extends Filter {
        //在这里定义过滤规则
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<Contact> list =null;
            //当过滤的关键字为空的时候，我们则显示所有的数据
            if (TextUtils.isEmpty(constraint)) {
                //list = backInfos;
            } else {
                //否则把符合条件的数据对象添加到集合中
                list = new ArrayList<>();   //新建ArrayList用于存放符合过滤条件的项
//                for (int i = 0; i < backInfos.length ; i++) {
//                    for (Contact c : backInfos[i]) {
//                        if (c.toString().contains(constraint)) {
//                            list.add(c);
//                        }
//                    }
//                }

            }
            results.values = list; //将得到的集合保存到FilterResults的value变量中
            results.count = list.size();//将集合的大小保存到FilterResults的count变量中
            return results; //返回results，publishResults(CharSequence constraint, FilterResults results)接受此返回值
        }

        //在这里更新显示结果
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //infos = (List<Contact>) results.values;   //取出results中存放的集合
            if (results.count > 0) {
                notifyDataSetChanged();//通知数据发生了改变
            } else {
                notifyDataSetInvalidated();//通知数据失效
            }
        }
    }
}
