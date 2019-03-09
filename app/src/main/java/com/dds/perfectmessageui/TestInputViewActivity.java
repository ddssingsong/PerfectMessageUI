package com.dds.perfectmessageui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.dds.messagelist.MessageList;
import com.dds.messagelist.MsgListAdapter;
import com.dds.messagelist.model.IMessage;
import com.gyf.barlibrary.ImmersionBar;

public class TestInputViewActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private MessageList messageList;
    private MsgListAdapter<IMessage> msgListAdapter;

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
    }


    private void initView() {
        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        messageList = findViewById(R.id.msg_list);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestInputViewActivity.this.finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();
    }

}
