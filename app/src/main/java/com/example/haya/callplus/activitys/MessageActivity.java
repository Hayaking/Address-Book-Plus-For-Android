package com.example.haya.callplus.activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.haya.callplus.R;
import com.example.haya.callplus.adapter.AdapterMessage;
import com.example.haya.callplus.beans.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessageActivity extends AppCompatActivity {
    private List<Message> messageList = new ArrayList<>();
    private RecyclerView messageRecylerView;
    private AdapterMessage messageAdapter;
    private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        initMsgs();
        editText = findViewById(R.id.msg_ed);
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            Toast.makeText(MessageActivity.this, "!!!", Toast.LENGTH_SHORT).show();
//                messageRecylerView.setLayoutParams(RelativeLayout.ABOVE);
        });
        messageRecylerView = findViewById(R.id.message_recylerView);
        messageRecylerView.setLayoutManager(new LinearLayoutManager(MessageActivity.this));
        messageAdapter = new AdapterMessage(messageList);
        messageRecylerView.setAdapter(messageAdapter);
    }

    private void initMsgs(){
        Message msg1=new Message("Hello guy.",Message.TYPE_RECEIVED);
        messageList.add(msg1);
        Message msg2=new Message("Hello.Who is that?",Message.TYPE_SENT);
        messageList.add(msg2);
        Message msg3=new Message("This is Tom!",Message.TYPE_RECEIVED);
        messageList.add(msg3);
    }

    public void send(View view) {
        Message msg2=new Message(editText.getText().toString(),Message.TYPE_SENT);
        editText.setText("");
        messageList.add(msg2);
        messageAdapter.notifyDataSetChanged();
    }
}
