package com.dds.perfectmessageui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.dds.chatinput.ChatInputView;
import com.dds.chatinput.listener.OnMenuClickListener;
import com.dds.chatinput.menu.MenuEventListener;
import com.dds.chatinput.menu.utils.CommonUtils;
import com.dds.chatinput.menu.view.MenuFeature;
import com.dds.chatinput.menu.view.MenuItem;
import com.dds.chatinput.model.FileItem;
import com.dds.messagelist.MessageList;
import com.dds.messagelist.MsgListAdapter;
import com.dds.messagelist.model.IMessage;
import com.dds.messagelist.model.MessageType;
import com.dds.perfectmessageui.bean.Message;
import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

public class TestInputViewActivity extends AppCompatActivity implements OnMenuClickListener {
    private static final String TAG = CommonUtils.formatTag(TestInputViewActivity.class.getSimpleName());

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
                .navigationBarColor(R.color.colorPrimary)
                .navigationBarDarkIcon(true)
                .init();
        setContentView(R.layout.activity_test_input_view);
        initView();
        initVar();
        initData();
    }

    private void initVar() {
        msgListAdapter = new MsgListAdapter<>(this, messageList);
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
                Log.d(TAG, "isStackFromEnd:" + messageList.isStackFromEnd());
                Log.d(TAG, "canScrollVertically:" + messageList.canScrollVertically());
                if (!messageList.isStackFromEnd()) {
                    if (messageList.canScrollVertically()) {
                        messageList.setStackFromEnd(true);
                    }
                }
                return true;
            }

            @Override
            public void onMenuFeatureVisibilityChanged(int visibility, String tag, MenuFeature menuFeature) {


            }
        });

        chatInputView.setMenuClickListener(this);
    }


    private void initData() {
        List<IMessage> list = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            Message message = new Message(MessageType.SEND_TEXT.value, "我是一条消息，" + i);
            list.add(message);
        }

        msgListAdapter.addToStartChronologically(list);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();
    }

    @Override
    public boolean onSendTextMessage(CharSequence input) {
        handleSendMsg(input.toString());
        return true;
    }

    private void handleSendMsg(String msg) {
        Message message = new Message(MessageType.SEND_TEXT.value, msg);
        msgListAdapter.addToEnd(message, true);
    }

    @Override
    public void onSendFiles(List<FileItem> list) {

    }

    @Override
    public boolean switchToAudioMode() {
        return false;
    }

    @Override
    public boolean switchToGalleryMode() {
        return false;
    }

    @Override
    public boolean switchToCameraMode() {
        return false;
    }

    @Override
    public boolean switchToEmojiMode() {
        return false;
    }

    @Override
    public void toggleSoftVisible() {
        Log.e(TAG, "toggleSoftVisible");
        Log.d(TAG, "isStackFromEnd:" + messageList.isStackFromEnd());
        Log.d(TAG, "canScrollVertically:" + messageList.canScrollVertically());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!messageList.isStackFromEnd()) {
                    if (messageList.canScrollVertically()) {
                        messageList.setStackFromEnd(true);
                    }
                }
            }
        }, 100);


    }

    @Override
    public void toggleSoftInVisible() {
        Log.e(TAG, "toggleSoftInVisible");

    }

    @Override
    public void onBackPressed() {
        if (chatInputView.isMenuFeatureVisible()) {
            chatInputView.dismissMenuLayout();
        } else {
            super.onBackPressed();
        }
    }

}
