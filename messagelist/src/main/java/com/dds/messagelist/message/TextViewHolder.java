package com.dds.messagelist.message;

import android.view.View;

import com.dds.messagelist.model.IMessage;

/**
 * Created by dds on 2019/2/18.
 * android_shuai@163.com
 */
public class TextViewHolder<MESSAGE extends IMessage> extends BaseMessageViewHolder<MESSAGE> {

    public TextViewHolder(View itemView, boolean isSender) {
        super(itemView, isSender);
    }

    @Override
    public void onBind(MESSAGE message) {


    }
}
