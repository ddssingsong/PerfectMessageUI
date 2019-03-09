package com.dds.perfectmessageui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.gyf.barlibrary.ImmersionBar;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .statusBarColor(R.color.colorPrimary)
                .statusBarDarkFont(true)
                .init();
        setContentView(R.layout.activity_main);


    }


    public void testMsgList(View view) {
        TestMsgListActivity.openActivity(this);


    }

    public void TestInputView(View view) {
        TestInputViewActivity.openActivity(this);
    }

    public void test2(View view) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();
    }
}
