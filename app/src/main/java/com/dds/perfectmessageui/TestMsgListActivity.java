package com.dds.perfectmessageui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.dds.messagelist.MessageList;
import com.dds.messagelist.message.MsgListAdapter;
import com.dds.messagelist.model.IMessage;
import com.dds.messagelist.model.MessageType;
import com.dds.perfectmessageui.bean.Message;
import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

public class TestMsgListActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private MessageList messageList;
    private MsgListAdapter<IMessage> msgListAdapter;

    public static void openActivity(Activity activity) {
        Intent intent = new Intent(activity, TestMsgListActivity.class);
        activity.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .statusBarColor(R.color.colorPrimary)
                .statusBarDarkFont(true)
                .init();
        setContentView(R.layout.activity_message);
        initView();
        initVar();
        initData();
    }


    private void initView() {
        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        messageList = findViewById(R.id.msg_list);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestMsgListActivity.this.finish();
            }
        });
    }

    private void initVar() {
        msgListAdapter = new MsgListAdapter<>(this);
        messageList.setAdapter(msgListAdapter);

    }

    private void initData() {
        List<IMessage> list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Message message = new Message(MessageType.SEND_TEXT.value, "hello");
            list.add(message);
        }

        msgListAdapter.addToEndChronologically(list);
    }

    boolean isSender = false;

    public void OnClickAdd(View view) {
        if (isSender) {
            Message message = new Message(MessageType.SEND_TEXT.value, "hello 我是发送者");
            msgListAdapter.addToStart(message, true);
            isSender = false;
        } else {
            Message message = new Message(MessageType.RECEIVE_TEXT.value, "hello，我是接收者");
            msgListAdapter.addToStart(message, true);
            isSender = true;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();
    }
}
