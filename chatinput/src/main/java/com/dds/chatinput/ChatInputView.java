package com.dds.chatinput;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * 聊天底部菜单和输入框
 * Created by dds on 2019/3/4.
 * android_shuai@163.com
 */
public class ChatInputView extends LinearLayout {


    public ChatInputView(Context context) {
        super(context);
        init(context);
    }

    public ChatInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    public ChatInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    private void init(Context context) {

    }

    private void initAttrs(Context context, AttributeSet attrs) {
    }

}
