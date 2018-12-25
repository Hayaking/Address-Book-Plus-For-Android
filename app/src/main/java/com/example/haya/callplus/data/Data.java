package com.example.haya.callplus.data;

import com.example.haya.callplus.R;
import com.example.haya.callplus.Utils.DBUtils;
import com.example.haya.callplus.beans.Contact;
import com.example.haya.callplus.beans.HistoricalRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Data {
     public final static int QR_CODE = 1;
     public static Contact self;
     public static Map<String, List<Contact>> infos = null;
     public static List<String> groupName = null;
     public static List<HistoricalRecord> historicalRecord;
     public static final List<Integer> ico ;
     static {
          ico = new ArrayList<>();
          ico.add(R.drawable.acc);
          ico.add(R.drawable.sex);
          ico.add(R.drawable.phone);
          ico.add(R.drawable.phone2);
          ico.add(R.drawable.qq);
          ico.add(R.drawable.email);
          ico.add(R.drawable.email);
     }
     public static void sync(){
          infos = DBUtils.queryAll();
          groupName = DBUtils.queryGroupName();
          historicalRecord = DBUtils.queryHistoricalRecord();
     }
}
