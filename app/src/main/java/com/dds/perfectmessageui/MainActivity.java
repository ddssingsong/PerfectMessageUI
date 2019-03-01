package com.dds.perfectmessageui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.StatusBarLightMode(this);
        setContentView(R.layout.activity_main);


    }


    public void testMsgList(View view) {
        TestMsgListActivity.openActivity(this);


    }

    public void test1(View view) {
    }

    public void test2(View view) {

    }
}
