package com.dds.perfectmessageui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class TestInputViewActivity extends AppCompatActivity {


    public static void openActivity(Activity activity) {
        Intent intent = new Intent(activity, TestInputViewActivity.class);
        activity.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_input_view);
    }
}
