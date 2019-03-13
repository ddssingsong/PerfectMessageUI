package com.dds.perfectmessageui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.dds.chatinput.ChatInputView;
import com.dds.chatinput.menu.Menu;
import com.dds.chatinput.menu.MenuEventListener;
import com.dds.chatinput.menu.view.MenuFeature;
import com.dds.chatinput.menu.view.MenuItem;
import com.dds.messagelist.MessageList;
import com.dds.messagelist.MsgListAdapter;
import com.dds.messagelist.model.IMessage;
import com.dds.messagelist.model.MessageType;
import com.dds.perfectmessageui.bean.Message;
import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

public class TestInputViewActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private MessageList messageList;
    private MsgListAdapter<IMessage> msgListAdapter;


    private ChatInputView chatInputView;

    public static void openActivity(Activity activity) {
        Intent intent = new Intent(activity, TestInputViewActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .statusBarColor(R.color.colorPrimary)
                .statusBarDarkFont(true)
                .init();
        setContentView(R.layout.activity_test_input_view);
        initView();
        initVar();
        initData();
    }

    private void initVar() {
        msgListAdapter = new MsgListAdapter<>(this);
        messageList.setAdapter(msgListAdapter);

    }

    private void initView() {
        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        messageList = findViewById(R.id.msg_list);
        chatInputView = findViewById(R.id.ci_chat_input_input);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestInputViewActivity.this.finish();
            }
        });

        chatInputView.setCustomMenuClickListener(new MenuEventListener() {
            @Override
            public boolean onMenuItemClick(String tag, MenuItem menuItem) {
                if (tag.equals(Menu.TAG_SEND)) {
                    handleSendMsg(chatInputView.getInputView().getText().toString());

                }

                return true;
            }

            @Override
            public void onMenuFeatureVisibilityChanged(int visibility, String tag, MenuFeature menuFeature) {

            }
        });
    }


    private void handleSendMsg(String msg) {
        Message message = new Message(MessageType.SEND_TEXT.value, msg);
        msgListAdapter.addToStart(message, true);
    }

    private void initData() {
        List<IMessage> list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Message message = new Message(MessageType.SEND_TEXT.value, "hello" + i);
            list.add(message);
        }

        msgListAdapter.addToEndChronologically(list);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();
    }

}
